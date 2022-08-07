package com.mysite.sbb33.controller;

import com.mysite.sbb33.service.ArticleService;
import com.mysite.sbb33.service.FileUploadService;
import com.mysite.sbb33.vo.ArticleWriteForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final ArticleService articleService;

    // 컨트롤러 부분. 그냥 get으로 받냐 post로 받냐 차이임!
    // get이랑 post는 전에 설명드렸어요! 생각하고 있는 그거 맞아요!
    @GetMapping("/multi-file")
    public String showMultiForm() {

        return "user/File.html";
    }

    @PostMapping("/multi-file")
    public String multiFileUpload(@RequestParam("multiFile") List<MultipartFile> multiFileList, ArticleWriteForm articleWriteForm, HttpServletRequest request, HttpSession session, Model model) throws InterruptedException {
        // 로그인 했는지 안했는지 검사하는 것.
        // 아마 형아는 시큐리티 이용해서 이 구문 사용 못해요...
        if (session.getAttribute("loginedUserId") == null) {
            // 로그인 안됬으면 경고창 출력하고 뒤로 돌아감
            model.addAttribute("msg", "로그인 부터 하세요.");
            model.addAttribute("historyBack", true);

            return "common/js";
        }

        // 파일이 있으면 파일과 함께 업로드 되고 파일이 없다면 글만 업로드 된다.
        // 현재는 유효성 검사 안해서 제목, 글 안써도 업로드됨;;
        try {
            // 받은 것을 서비스로 넘김.
            // 컨트롤러에서는 기능을 처리하는거 아님.
            // 컨트롤러 쓰는 이유는 여러가지 있지만 보안 때문에 쓰는 것도 큼.
        /*
        articleForm : 글쓰기에서 제목과 내용을 얻기위해 사용
        multiFileList : 파일을 담은 리스트
        request : Http에서 쿠기와 같은 정보, 요청을 받은 것
        session : 쿠키의 보안버전
        (스프링부트는 바보라 기억력이 없다. 따라서 웹페이지는 쿠키에 정보를 담고 있다가 스프링 부트에 요청할 때 자기가 가지고 있는 쿠키를 준다. 쿠키 맛있겠네)
        (쿠키에 자세한 설명을 원하면 '알려줘'를 외쳐라. 친절히 알려주겠다.)
         */
            fileUploadService.doUpload(articleWriteForm, multiFileList, request, session);

        } catch(Exception e) {
            articleService.doWrite((long) session.getAttribute("loginedUserId"), articleWriteForm.getTitle(), articleWriteForm.getBody());
        }
        // 경고 메시지 출력하고 메인페이지로 보내버림


        return "redirect:/";
    }
}
