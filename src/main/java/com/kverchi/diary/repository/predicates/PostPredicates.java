package com.kverchi.diary.repository.predicates;

import com.kverchi.diary.model.entity.QPost;
import com.kverchi.diary.model.enums.PostSearchCriteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import java.util.Map;

/**
 * Created by Kverchi on 18.7.2018.
 */
public class PostPredicates {
    private static QPost post = QPost.post;

    public static Predicate searchPost(Map<PostSearchCriteria, Object> searchAttributes) {
        BooleanBuilder builder = new BooleanBuilder();

        for (Map.Entry<PostSearchCriteria, Object> entry : searchAttributes.entrySet()) {
            switch (entry.getKey()) {
                case BY_AUTHOR_ID:
                    builder.and(post.user.userId.eq((Integer)entry.getValue()));
                    break;
                case BY_SIGHT_ID:
                    builder.and(post.countriesSight.sightId.eq((Integer)entry.getValue()));
                    break;
                case BY_TEXT:
                    //TODO search in text, title and description
                    builder.and(post.text.containsIgnoreCase((String)entry.getValue()));
                    break;
            }
        }
        return builder;
    }

}
