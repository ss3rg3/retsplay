package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import helpers.Helpers;
import models.BlogPost;
import models.DbMock;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.util.List;

public class Blog extends Controller {

    @Inject
    private FormFactory formFactory;

    private boolean produceErrorResponse = true;


    public Result getAllPosts() {

        final List<BlogPost> blogPostList = DbMock.getAllPostsAsList();

        return this.simulateRealWorldResponse(Json.toJson(blogPostList));
    }

    public Result getPost(final String id) {

        final BlogPost blogPost = DbMock.store.get(id);

        return this.simulateRealWorldResponse(Json.toJson(blogPost));
    }

    public Result addNewPost() {

        final BlogPost blogPost = formFactory.form(BlogPost.class).bindFromRequest().get();

        DbMock.store.put(blogPost.getId(), blogPost);

        return this.simulateRealWorldResponse(null);
    }

    public Result deletePost(final String id) {

        DbMock.store.remove(id);

        return this.simulateRealWorldResponse(null);
    }

    public Result updatePost(final String id) {

        final BlogPost blogPost = formFactory.form(BlogPost.class).bindFromRequest().get();
        DbMock.store.put(blogPost.getId(), blogPost);

        return this.simulateRealWorldResponse(null);
    }



    public Result simulateRealWorldResponse(final JsonNode jsonNode) {
        System.out.println("Next request will produce error? " + this.produceErrorResponse);
//        this.produceErrorResponse = !this.produceErrorResponse; // false;
        this.produceErrorResponse = false;
        Helpers.sleep(500);
        return this.produceErrorResponse ? Results.badRequest() : jsonNode != null ? Results.ok(jsonNode) : Results.ok();
    }


}

