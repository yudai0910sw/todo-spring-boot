package org.example.crudapp.controller;

import java.util.List;
import java.util.Map;

import org.example.crudapp.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class TodoController {

    private JdbcTemplate jdbcTemplate;

    //コンストラクタ
    @Autowired
    public TodoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //TOPページの表示
    @GetMapping
    public String index(Model model) {
        return "sample/index";
    }

    //TODO一覧の表示
    @GetMapping("/todos")
    public String todoIndex(Model model) {
        System.out.println("todosの取得");
        String sql = "SELECT * FROM todo";
        List<Map<String, Object>> todos = jdbcTemplate.queryForList(sql);
        System.out.println(todos);
        model.addAttribute("todos", todos);
        model.addAttribute("todo", new Todo()); // 新規作成用の空のTodoオブジェクトを追加
        return "todos/index";
    }

    //TODOの作成
    @PostMapping("/todo/create")
    public String createTodo(Todo todo) {
        System.out.println("todoの作成");
        String sql = "INSERT INTO todo(title, body) VALUES (?, ?)";
        jdbcTemplate.update(sql, todo.getTitle(), todo.getBody());
        return "redirect:/todos";
    }

    //TODOの削除
    @DeleteMapping("/todo/{id}")
    public String todoDelete(@PathVariable("id") int id) {
        System.out.println("todoの削除");
        String sql = "DELETE FROM todo WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return "redirect:/todos";
    }

    //todo編集ページの表示
    @GetMapping("/todo/{id}")
    public String todoEdit(@ModelAttribute Todo todo, @PathVariable int id) {
        System.out.println("todoの取得");
        String sql = "SELECT * FROM todo WHERE id = " + id;
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        todo.setId(((Number)map.get("id")).intValue());
        todo.setTitle((String)map.get("title"));
        todo.setBody((String)map.get("body"));
        return "/todos/edit";
    }

    //Todo編集データの保存
    @PutMapping("/todo/{id}")
    public String todoUpdate(Todo todo, @PathVariable int id) {
        System.out.println("todoの更新");
        String sql = "UPDATE todo SET title = ?, body = ? WHERE id = " + id;
        jdbcTemplate.update(sql, todo.getTitle(), todo.getBody());
        return "redirect:/todos";
    }

    // Todoのステータス更新
    @PostMapping("/todo/status/{id}")
    public String updateTodoStatus(@PathVariable("id") int id, @RequestParam("done") int done) {
        System.out.println("Statusの更新");
        String sql = "UPDATE todo SET done = ? WHERE id = ?";
        jdbcTemplate.update(sql, done, id);
        return "redirect:/todos";
    }

//    //新規入力フォームの表示
//    @GetMapping("/test/form")
//    public String form(@ModelAttribute TestForm testForm) {
//        return "sample/form";
//    }
//
//    //新規入力データの保存
//    @PostMapping("/test/form")
//    public String create(TestForm testForm) {
//        String sql = "INSERT INTO test_table(name, old) VALUES(?, ?);";
//        jdbcTemplate.update(sql, testForm.getName(), testForm.getOld());
//        return "redirect:/sample";
//    }
//
//    //編集フォームの表示
//    @GetMapping("/edit/{id}")
//    public String edit(@ModelAttribute TestForm testForm, @PathVariable int id) {
//        String sql = "SELECT * FROM test_table WHERE id = " + id;
//        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
//        testForm.setId((int)map.get("id"));
//        testForm.setName((String)map.get("name"));
//        testForm.setOld((int)map.get("old"));
//        return "sample/edit";
//    }
//
//    //編集データの保存
//    @PostMapping("/edit/{id}")
//    public String update(TestForm testForm, @PathVariable int id) {
//        String sql = "UPDATE test_table SET name = ?, old = ? WHERE id = " + id;
//        jdbcTemplate.update(sql, testForm.getName(), testForm.getOld());
//        return "redirect:/sample";
//    }
//
//    //データの削除
//    @PostMapping("/delete/{id}")
//    public String delete(@PathVariable int id) {
//        String sql = "DELETE from test_table WHERE id = " + id;
//        jdbcTemplate.update(sql);
//        return "redirect:/sample";
//    }
}
