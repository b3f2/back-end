package com.backend.api.repository.post;

import com.backend.api.dto.post.PostReadCondition;
import com.backend.api.dto.post.PostSimpleDto;
import com.backend.api.entity.post.Post;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.backend.api.entity.post.QPost.post;
import static com.querydsl.core.types.Projections.constructor;

@Transactional(readOnly = true)
public class CustomPostRepositoryImpl extends QuerydslRepositorySupport implements CustomPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomPostRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Post> findAllByCondition(PostReadCondition cond) {
//        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
//        Predicate predicate = createPredicate(cond);
        return jpaQueryFactory.selectFrom(post)
                .limit(cond.getSize())
                .offset(cond.getOffset())
                .orderBy(post.id.desc())
                .fetch();
    }

    private List<PostSimpleDto> fetchAll(Predicate predicate, Pageable pageable) {
        List<PostSimpleDto> fetch = getQuerydsl().applyPagination(
                pageable,
                jpaQueryFactory
                        .select(constructor(PostSimpleDto.class, post.id, post.title, post.user.nickName, post.createTime))
                        .from(post)
                        .join(post.user)
                        .where(predicate)
                        .orderBy(post.id.desc())
        ).fetch();
        return
                fetch;
    }

    private Predicate createPredicate(PostReadCondition cond) {
        return new BooleanBuilder();
    }
    private Long fetchCount(Predicate predicate) {
        return jpaQueryFactory.select(post.count()).from(post).where(predicate).fetchOne();
    }


    private Predicate orConditionsByEqUserIds(List<Long> userIds) {
        return orConditions(userIds, post.user.id::eq);
    }

    private <T> Predicate orConditions(List<T> values, Function<T, BooleanExpression> term) {
        return values.stream()
                .map(term)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }
}
