package com.backend.api.service.post;

import com.backend.api.dto.post.*;
import com.backend.api.entity.post.*;
import com.backend.api.entity.user.User;
import com.backend.api.exception.*;
import com.backend.api.repository.comment.CommentRepository;
import com.backend.api.repository.category.CategoryRepository;
import com.backend.api.repository.post.ImageRepository;
import com.backend.api.repository.post.PostLikeRepository;
import com.backend.api.repository.post.PostRepository;
import com.backend.api.repository.user.UserRepository;
import com.backend.api.request.post.PostCreateRequest;
import com.backend.api.request.post.PostUpdateRequest;
import com.backend.api.response.comment.CommentResponse;
import com.backend.api.response.post.*;
import com.backend.api.response.user.LoginResponse;
import com.backend.api.service.file.AwsS3UploadService;
import com.backend.api.service.file.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final AwsS3UploadService s3UploadService;
    private final FileUploadService fileUploadService;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;

    private final static String PROCESS_LIKE_POST = "좋아요 처리 완료";
    private final static String PROCESS_UNLIKE_POST = "좋아요 취소 완료";

    @Transactional
    public PostCreateResponse create(LoginResponse loginResponse, PostCreateRequest req, List<MultipartFile> images) throws IOException {
        List<Image> imageList = new ArrayList<>();
        List<ImageDto> imageDtoList = new ArrayList<>();
        if(!images.isEmpty()) {
            for (MultipartFile image : images) {
                ImageDto imageDto = fileUploadService.uploadImage(image, "post");
                imageDtoList.add(imageDto);
            }
        }

        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);
        dtoParser(imageList, imageDtoList);

        Post post = Post.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .user(user)
                .category(category)
                .images(imageList)
                .build();

        Post save = postRepository.save(post);
        return new PostCreateResponse(save.getId());
    }

    @Transactional(readOnly = true)
    public PostDetailResponse read(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        //imageList
        List<String> imgUrl = imageRepository.findAllByPost(post)
                .stream()
                .map(Image::getImgUrl)
                .collect(Collectors.toList());

        //commentList
        List<Comment> findCommentByPost = commentRepository.findAllByPost(post);
        List<CommentResponse> commentList = new ArrayList<>();

        for(Comment comment : findCommentByPost) {
            commentList.add(new CommentResponse(comment));
        }

        PostDetailResponse postDetailResponse = PostDetailResponse.builder()
                .postId(id)
                .userId(post.getUser().getId())
                .nickname(post.getUser().getNickName())
                .content(post.getContent())
                .title(post.getTitle())
                .createTime(post.getCreateTime())
                .imgList(imgUrl)
                .commentList(post.getComments())
                .categoryId(post.getCategory().getId())
                .categoryname(post.getCategory().getName())
                .build();

        return postDetailResponse;
    }

    public List<PostResponse> readAll(PostReadCondition cond) {
        return postRepository.findAllByCondition(cond).stream().map(PostResponse::new).collect(toList());
    }

    @Transactional
    public void delete(Long id, LoginResponse loginResponse) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);

        //본인 post인지 확인
        if(!post.getUser().equals(user)) {
            throw new NotYourPostException();
        }

        //post 삭제시 s3에 저장된 이미지도 삭제
        List<Image> imageList = imageRepository.findAllByPost(post);
        for(Image image : imageList) {
            s3UploadService.deleteFile(image.getImgName());
            s3UploadService.deleteFile(image.getImgName().replace("post/", "post-resized/"));
        }

        postLikeRepository.deleteAllByPost(post);
        postRepository.deleteById(post.getId());
    }

    @Transactional
    public PostUpdateResponse update(Long id, LoginResponse loginResponse, PostUpdateRequest req, List<MultipartFile> imgs) throws IOException {

        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);

        //본인 게시글만 수정 가능
        if(!post.getUser().equals(user)) {
            throw new NotYourPostException();
        }

        List<Image> imageList = post.getImages();
        List<ImageDto> imageDtoList = new ArrayList<>();
        List<Image> removeImageList = new ArrayList<>();

        //수정할 이미지 s3, 이미지 db에서 삭제
        for(Image image:imageList) {
            for(String imgName:req.getImgName()) {
                if(!image.getImgName().equals(imgName)) {
                    removeImageList.add(image);
                    s3UploadService.deleteFile(image.getImgName());
                    s3UploadService.deleteFile(image.getImgName().replace("post/", "post-resized"));
                    imageRepository.deleteById(image.getId());
                }
            }
        }

        //removeImageList에 담긴 수정 이미지 원래 imageList에서 삭제
        for(Image image : removeImageList) {
            imageList.remove(image);
        }

        //추가할 이미지 S3에 추가
        if(imgs != null) {
            for(MultipartFile img : imgs) {
                if(!img.isEmpty()) {
                    ImageDto imageDto = fileUploadService.uploadImage(img, "post");
                    imageDtoList.add(imageDto);
                }
            }
        }
        putDtoParser(imageList, imageDtoList);
        post.updatePost(req, imageList);
        return new PostUpdateResponse(id);
    }

    private void dtoParser(List<Image> imageList, List<ImageDto> imageDtoList) {
        for(ImageDto imageDto : imageDtoList) {
            Image image = Image.builder()
                    .imgName(imageDto.getImageName())
                    .imgUrl(imageDto.getImgUrl())
                    .build();
            imageRepository.save(image);
            imageList.add(image);
        }
    }

    private void putDtoParser(List<Image> imageList, List<ImageDto> imageDtoList) {
        for(ImageDto imageDto : imageDtoList) {
            Image image = Image.builder()
                    .imgName(imageDto.getImageName())
                    .imgUrl(imageDto.getImgUrl())
                    .build();
            imageList.add(image);
            imageRepository.save(image);
        }
    }

    @Transactional
    public String likePost(Long id, LoginResponse loginResponse) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);
        PostLike postLike = new PostLike(post, user);
        postLikeRepository.save(postLike);
        return PROCESS_LIKE_POST;
    }

    @Transactional
    public String updatePostLike(Long id, LoginResponse loginResponse) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        User user = userRepository.findByEmail(loginResponse.getEmail()).orElseThrow(UserNotFoundException::new);
        if(!hasLikePost(post, user)) {
            post.increaseLikeCount();
            return likePost(id, loginResponse);
        }
        post.decreaseLikeCount();
        return removeLikePost(post, user);
    }

    public boolean hasLikePost(Post post, User user) {
        return postLikeRepository.findByPostAndUser(post, user).isPresent();
    }

    private String removeLikePost(Post post, User user) {
        PostLike postLike = postLikeRepository.findByPostAndUser(post, user).orElseThrow(PostLikeNotFoundException::new);
        postLikeRepository.delete(postLike);
        return PROCESS_UNLIKE_POST;
    }

}
