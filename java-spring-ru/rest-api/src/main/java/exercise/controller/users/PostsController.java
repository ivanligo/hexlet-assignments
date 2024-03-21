package exercise.controller.users;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {

    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();

    @GetMapping("users/{id}/posts") // список всех постов, написанных пользователем с таким же userId, как id в маршруте
    @ResponseStatus(HttpStatus.OK)
    public List<Post> index(@RequestParam(defaultValue = "10") Integer limit,
                            @RequestParam(defaultValue = "1") Integer page,
                            @PathVariable int id) {
        List<Post> post = posts.stream()
                .filter(p -> p.getUserId() == id).collect(Collectors.toList());
        return post;
    }

    @PostMapping("users/{id}/posts") // создание нового поста, привязанного к пользователю по id. Код должен возвращать статус 201
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post,@PathVariable int id ) {
        post.setUserId(id);
        posts.add(post);
        return post;
    }
}
// END
