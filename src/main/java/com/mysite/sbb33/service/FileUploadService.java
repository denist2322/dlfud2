package com.mysite.sbb33.service;

import com.mysite.sbb33.repository.FilesRepository;
import com.mysite.sbb33.vo.Article;
import com.mysite.sbb33.vo.ArticleWriteForm;
import com.mysite.sbb33.vo.Files;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final FilesRepository filesRepository;
    private final ArticleService articleService;

    @Async
    public void doUpload(ArticleWriteForm articleWriteForm, List<MultipartFile> multiFileList, HttpServletRequest request, HttpSession session) {
        // 파일을 저장할 주소를 설정한다.
        String root = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\uploadFiles";

        // 아래 두 줄은 경로가 실제로 존재하는지 확인한다.
        // 만약 실제로 존재하지 않는다면 폴더를 생성한다. (mkdir가 폴더 생성이다.)
        File fileCheck = new File(root);
        if (!fileCheck.exists()) fileCheck.mkdirs();

        //원래 파일의 이름 -> 수정된 파일의 이름을 여러개 담기위해 리스트로 만듬
        List<Map<String, String>> fileList = new ArrayList<>();

        for (int i = 0; i < multiFileList.size(); i++) {
            // 파일의 본래이름을 가져옴
            String originFile = multiFileList.get(i).getOriginalFilename();
            // 파일형식을 ext에 뽑아냄 ex) 나는이미지.png 에서 .png를 뽑아서 ext에 저장
            String ext = originFile.substring(originFile.lastIndexOf("."));
            // uuid는 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 규약이다.
            // 다시말해 파일들이 서로 이름을 겹치지 않게 하기 위해서 만든것이다. 마지막에 ext를 붙여 파일형식을 정의해준다.
            String changeFile = UUID.randomUUID().toString() + ext;

            // 본래 이름을 Key, 바꾼 이름을 Value로 해서 map에 저장한다.
            Map<String, String> map = new HashMap<>();
            map.put("originFile", originFile);
            map.put("changeFile", changeFile);

            // 수정된 파일의 이름들을 담고 있는 map을 리스트에 저장한.
            fileList.add(map);
        }

        // 파일 업로드를 진행함
        try {
            for (int i = 0; i < multiFileList.size(); i++) {
                /*
                transferTo 는 파일을 저장하는 함수이다.
                파일 리스트에서 파일을 뽑고, 그 파일을 지정한 경로에 저장한다.
                */
                File uploadFile = new File(root + "\\" + fileList.get(i).get("changeFile"));
                multiFileList.get(i).transferTo(uploadFile);
            }
            // 위 작업이 완료되었다면 성공적으로 파일을 저장했다는 뜻으로, 이제 글을 생성해야한다.
            // 아래에 있는 함수는 현재 클래스에서 지원한다.
            // 파일 목록, 세션정보, 받아온 제목과 내용을 넘긴다.
            uploadDB(fileList, session, articleWriteForm);
            // 확인용...
            System.out.println("다중 파일 업로드 성공!");

        } catch (IllegalStateException | IOException e) {
            // 확인용...
            System.out.println("다중 파일 업로드 실패 ㅠㅠ");
            // 만약 업로드 실패하면 파일 삭제
            for (int i = 0; i < multiFileList.size(); i++) {
                new File(root + "\\" + fileList.get(i).get("changeFile")).delete();
            }
            // 오류 추적하는거임
            e.printStackTrace();
        }
    }

    // 파일 DB에 업로드 할거임.
    private void uploadDB(List<Map<String, String>> fileName, HttpSession session, ArticleWriteForm articleWriteForm) {

        // 먼저 글쓰기를 진행함. 로그인한 아이디와 제목, 내용을 넘긴다. (로그인한 아이디를 넘긴 이유는 누가 글을 썼는지 알기 위해서이다.)
        Article aritcle = articleService.doWrite((long) session.getAttribute("loginedUserId"), articleWriteForm.getTitle(), articleWriteForm.getBody());
        // 파일을 만들어서 DB에 저장하는 과정
        for (Map<String, String> file : fileName) {
            // 우리는 DB에 filename만 넣어주면 됨. 왜 그런가 생각이 들면 옆에 DB도 넣어놨으니 한번 보세요!
            // 이거 잘 이해 안되시면 질문하세요. 근데 그대는 잘 알잖아?
            Files files = new Files();
            files.setFilename(file.get("changeFile"));
            files.setArticle(aritcle);
            filesRepository.save(files);

        }
    }
}
