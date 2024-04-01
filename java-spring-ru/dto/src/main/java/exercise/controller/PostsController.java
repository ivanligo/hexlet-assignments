package exercise.controller;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping()
    public List<PostDTO> index(){
        List<Post> posts = postRepository.findAll();
        List<PostDTO> dtoPostList = new ArrayList<>();

        for (Post post: posts){
            dtoPostList.add(toDtoPost(post));
        }
        return dtoPostList;
    }

    @GetMapping("/{id}")
    public PostDTO show(@PathVariable Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));

        return toDtoPost(post);
    }

    private PostDTO toDtoPost(Post post){
        PostDTO dtoPost = new PostDTO();
        dtoPost.setId(post.getId());
        dtoPost.setTitle(post.getTitle());
        dtoPost.setBody(post.getBody());
        dtoPost.setComments(commentRepository.findByPostId(post.getId()).stream()
                .map(this::toDtoComment)
                .toList());

        return dtoPost;
    }
    private CommentDTO toDtoComment(Comment comment){
        CommentDTO dtoComment = new CommentDTO();
        dtoComment.setId(comment.getId());
        dtoComment.setBody(comment.getBody());
        return dtoComment;
    }

}



// END
