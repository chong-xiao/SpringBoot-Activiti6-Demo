package com.cazj;

import static org.assertj.core.api.Assertions.contentOf;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cazj.dao.NoticeDao;
import com.cazj.pojo.Notice;

/**
 * 测试类
 * @author UID李东起
 */
@SpringBootTest
public class NoticeTest {
	@Autowired
	private NoticeDao noticeDao;
	@Test
	public void noticeTest() {
		Notice content = noticeDao.findNoticeById(4);
		System.out.println(content);
	}
}
