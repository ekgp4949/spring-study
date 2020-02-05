package com.eomcs.lms.servlet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.eomcs.lms.dao.BoardFileDao;
import com.eomcs.lms.domain.Board;

public class BoardDetailServlet implements Servlet {
  BoardFileDao boardDao;

  public BoardDetailServlet(BoardFileDao boardDao) {
    this.boardDao = boardDao;
  }

  @Override
  public void service(ObjectInputStream in, ObjectOutputStream out) throws Exception {
    int no = in.readInt();
    Board board = boardDao.findByNo(no);
    if (board != null) {
      out.writeUTF("OK");
      out.writeObject(board);
    } else {
      out.writeUTF("FAIL");
      out.writeUTF("해당번호의 게시물이 없습니다.");
    }
  }
}
