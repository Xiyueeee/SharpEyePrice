package com.example.sharpeyeprice.controller;

//实现Restful API的包：是一种基于 HTTP 协议设计的、用于进行数据交换和通信的 Web 服务接口。
import com.example.sharpeyeprice.utils.APIResponse;
import com.example.sharpeyeprice.model.User;
import com.example.sharpeyeprice.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//SHA-256加密的包
import com.example.sharpeyeprice.utils.Encoder;

//JWT的包
import com.example.sharpeyeprice.utils.JwtUtil;

//正则表达式的包
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;

//发送邮件的包
import com.example.sharpeyeprice.utils.EmailSender;

import javax.ws.rs.core.Response;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    //注册接口
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        // 创建一个Map来存储返回的消息
        Map<String, Object> response = new HashMap<>();

        // 打印接收到的用户信息
        log.info("Received account: {}", user.getAccount());
        log.info("Received password: {}", user.getPassword());
        log.info("Received email: {}", user.getEmail());

        // 若账户已存在则创建失败
        String res_account = userService.check_account(user.getAccount());
        if (res_account != null) {
            response.put("success", false);
            response.put("message", "账户已存在");
            return ResponseEntity.ok(response);
        }

        // 若邮箱已存在则创建失败
        String res_email = userService.check_email(user.getEmail());
        if (res_email != null) {
            response.put("success", false);
            response.put("message", "邮箱已被注册");
            return ResponseEntity.ok(response);
        }

        // 检查账户/密码是否过短
        if (user.getAccount().length() < 6) {
            response.put("success", false);
            response.put("message", "账户名过短");
            return ResponseEntity.ok(response);
        }
        if (user.getPassword().length() < 6) {
            response.put("success", false);
            response.put("message", "密码过短");
            return ResponseEntity.ok(response);
        }

        // 检查邮箱格式
        String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = EMAIL_PATTERN.matcher(user.getEmail());

        if (!matcher.matches()) {
            response.put("success", false);
            response.put("message", "邮箱格式有误");
            return ResponseEntity.ok(response);
        }

        // 注册用户
        userService.register(user);

        // 返回成功信息
        response.put("success", true);
        response.put("message", "注册成功");
        return ResponseEntity.ok(response);
    }

    //登录接口
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        //获取前端传来的用户名和密码
        String account = user.getAccount();
        String password = user.getPassword();


        //打印接收到的信息
        log.info("Received account: {}", account);
        log.info("Received password: {}", password);

        //校验账户是否存在
        if(!userService.isAccountExist(account)) {
            response.put("success", false);
            response.put("message", "账户不存在");
            return ResponseEntity.ok(response);
        }

        //校验密码是否正确
        if(!userService.checkPassword(account, password)) {
            response.put("success", false);
            response.put("message", "密码错误");
            return ResponseEntity.ok(response);
        }

        //登录成功
        response.put("success",true);
        response.put("message", "登录成功");
        User loggedInUser = userService.find_user_by_account(account);
        session.setAttribute("loggedInUser", loggedInUser);
        response.put("user", loggedInUser);  //直接返回用户信息
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        //从session中获取当前用户信息（假设在用户登录时存储了用户信息）
        User loggedInUser = (User) session.getAttribute("loggedInUser");

//        if(loggedInUser == null) {
//            response.put("success",false);
//            response.put("message", "用户未登录");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//        }

        //返回用户信息
        response.put("success",true);
        response.put("message","获取用户信息成功");
        response.put("user",loggedInUser);
        return ResponseEntity.ok(response);
    }
}

/*
@RestController
public class UserController {
    //http://localhost:8080/hello
    //http://localhost:8080/hello?nickname=zhangsan&phone=123   前端传递过来的参数,多个参数用&拼接
    @RequestMapping("/hello")   //浏览器一会要发送get请求来访问hello这个方法
    public String hello(String nickname,String phone){
        System.out.println(phone);
        return "你好" + nickname;
    }
}
//GET用于获取资源，POST用于新建资源，PUT用于更新资源，DELETE用于删除资源
*/
/*
@RequestMapping("/user")   ///user
public class UserController {
    @Autowired
    private UserService userService;

    //登录功能
    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestParam String account, @RequestParam String password) {
        String res_password = userService.login(account);
        if (res_password == null)
            return ResponseEntity.ok(new APIResponse("用户不存在", 200));
        else {
            if (!res_password.equals(Encoder.sha256(password)))
                return ResponseEntity.ok(new APIResponse("密码错误",200));
            else {
                String jwt_value = JwtUtil.createJWT("login", account, password);
                return ResponseEntity.ok(new APIResponse("登录成功",200,jwt_value));
            }
        }
    }

    public boolean isInvalidEmail(String email) {
        String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return !matcher.matches();
    }
    //注册功能
    @PostMapping("/register")
    public ResponseEntity<APIResponse> register(@RequestParam String account, @RequestParam String password, @RequestParam String email, @RequestParam String code, @RequestParam String jwt_value) {
        //若存在账户则创建失败
        String res_account = userService.check_account(account);
        if (res_account != null)
            return ResponseEntity.ok(new APIResponse("账户已存在", 200));

        //若存在邮箱则创建失败
        String res_email = userService.check_email(email);
        if (res_email != null)
            return ResponseEntity.ok(new APIResponse("邮箱已被注册", 200));

        //检查账户/密码是否太短（账户后续可考虑不加限制）
        if (account.length() < 6)
            return ResponseEntity.ok(new APIResponse("账户名过短", 200));
        if (password.length() < 6)
            return ResponseEntity.ok(new APIResponse("密码过短",200));

        //检查邮箱格式
        if (isInvalidEmail(email))
            return ResponseEntity.ok(new APIResponse("邮箱格式有误", 200));
        try {
            if (!JwtUtil.paraJWT2code(jwt_value).equals(Encoder.sha256(code))) {
                return ResponseEntity.ok(new APIResponse("验证码有误", 200));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new APIResponse("验证码已失效， 请重新发送", 200));
        }

        User user = new User(1,account,Encoder.sha256(password),email);
        userService.register(user);
        return ResponseEntity.ok(new APIResponse("注册成功", 200));
    }

    //重置密码
    @GetMapping("/send_email")
    public ResponseEntity<APIResponse> send_email(@RequestParam String email) {
        User res_user = userService.check_user_by_email(email);
        if (isInvalidEmail(email)){
            return ResponseEntity.ok(new APIResponse("邮箱格式不正确，请重新输入", 200));
        } else {
            String code = EmailSender.code_sender(email);
            String jwt_value = JwtUtil.createJWT("email", email, code);
            System.out.println(jwt_value);
            APIResponse response = new APIResponse("发送成功", 200, jwt_value);
            return ResponseEntity.ok(response);
        }
    }
    @PostMapping("/reset_password")
    public ResponseEntity<APIResponse> reset_password(@RequestParam String account, @RequestParam String new_password, @RequestParam String email, @RequestParam String code, @RequestParam String jwt_value) {
        User res_user = userService.find_user_by_account(account);
        try {
            if (!JwtUtil.paraJWT2code(jwt_value).equals(Encoder.sha256(code))) {
                return ResponseEntity.ok(new APIResponse("验证码错误", 200));
            }
        } catch (Exception e){
            return ResponseEntity.ok(new APIResponse("验证码已失效，请重新发送", 200));
        }
        if (res_user == null)
            return ResponseEntity.ok(new APIResponse("用户名不存在", 200));
        else if (!res_user.getEmail().equals(email))
            return ResponseEntity.ok(new APIResponse("邮箱与账户名不匹配",200));
        else if (new_password.length() < 6)
            return ResponseEntity.ok(new APIResponse("新密码太短，不能少于6字符",200));
        else {
            userService.update_password_by_account(account, Encoder.sha256(new_password));
            return ResponseEntity.ok(new APIResponse("重置密码成功",200));
        }
    }
}
*/