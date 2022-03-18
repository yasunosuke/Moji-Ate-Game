package com.MojiAteGame;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class QuestionWordDAO {
	
	private static final String URL;
	private static final String USER;
	private static final String PASSWORD;
	
	static {
		Properties props = new Properties();
		
		try {
			props.load(new FileInputStream("src/sql.properties"));
		} catch (IOException e) {
			System.out.println("file load error");
		}
		
		URL = props.getProperty("url");
		USER = props.getProperty("user");
		PASSWORD = props.getProperty("password");
	}
	
	public List<QuestionWordDTO> getWordsAtLevel(int level) {
		
	}

	public Stage getStage(int stageNumber) {

		List<String> words = new ArrayList<String>();
		long timelimit = 0;

		try(Connection con = DriverManager.getConnection(URL, USER, PASSWORD); 
			PreparedStatement pstmt = con.prepareStatement("SELECT * from stages WHERE stage_id = ?");) {
			
			pstmt.setInt(1, stageNumber);
			
			try(ResultSet rs = pstmt.executeQuery()) {
				while(rs.next()) {
//					データベースから取得した単語をListに入れる。
					for(int i = 1; i <= 10; i++) {
						words.add(rs.getString("word" + i));
					}
					timelimit = rs.getLong("time_limit");
				}
			} catch(SQLException e) {
				System.out.println("エラーコードは" + e.getErrorCode());
			}

		} catch(SQLException e) {
			System.out.println("エラー" + e.getErrorCode());
		}

		return new Stage(stageNumber, timelimit, words);
	}
}
