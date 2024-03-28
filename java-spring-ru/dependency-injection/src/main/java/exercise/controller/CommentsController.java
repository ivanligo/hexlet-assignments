package exercise.controller;

import exercise.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("/comments")
public class CommentsController {
    @Autowired
    private CommentRepository commentRepository;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Comment> index(){
        return commentRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Comment show(@PathVariable long id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));
        return comment;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Comment create(@RequestBody Comment comment){
        commentRepository.save(comment);
        return comment;
    }

    @PutMapping(path = "/{id}")
    public Comment update(@PathVariable long id,@RequestBody Comment data){
        Comment maybeComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));
        if (maybeComment != null){
            data.setId(id);
            Comment comment = maybeComment;
            comment.setId(data.getId());
            comment.setPostId(data.getPostId());
            comment.setBody(data.getBody());
            commentRepository.save(comment);
        }
        return data;
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable long id){
        commentRepository.deleteById(id);
    }
}
// END
