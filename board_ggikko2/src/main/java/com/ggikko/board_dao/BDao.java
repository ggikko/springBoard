package com.ggikko.board_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.ggikko.board_dto.BDto;
import com.ggikko.board_util.Constant;

public class BDao {

	DataSource dataSource;
	JdbcTemplate template = null;

	public BDao() {

		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context
					.lookup("java:comp/env/jdbc/ggikko");
		} catch (Exception e) {
			e.printStackTrace();
		}

		template = Constant.template;

	}

	public ArrayList<BDto> list() {

		String query = "select bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent from ggikko_board order by bGroup desc, bStep asc";
		return (ArrayList<BDto>) template.query(query,
				new BeanPropertyRowMapper<BDto>(BDto.class));

		/*
		 * Connection connection = null; String query =
		 * "select bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent from ggikko_board order by bGroup desc, bStep asc"
		 * ; PreparedStatement preparedStatement = null; ResultSet resultSet =
		 * null;
		 * 
		 * try { connection = dataSource.getConnection(); preparedStatement =
		 * connection.prepareStatement(query); resultSet =
		 * preparedStatement.executeQuery();
		 * 
		 * while (resultSet.next()) { int bId = resultSet.getInt("bId"); String
		 * bName = resultSet.getString("bName"); String bTitle =
		 * resultSet.getString("bTitle"); String bContent =
		 * resultSet.getString("bContent"); Timestamp bDate =
		 * resultSet.getTimestamp("bDate"); int bHit = resultSet.getInt("bHIt");
		 * int bGroup = resultSet.getInt("bGroup"); int bStep =
		 * resultSet.getInt("bStep"); int bIndent = resultSet.getInt("bIndent");
		 * 
		 * BDto dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit,
		 * bGroup, bStep, bIndent); dtos.add(dto);
		 * 
		 * }
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } finally { try { if
		 * (resultSet != null) resultSet.close(); if (connection != null)
		 * connection.close(); if (preparedStatement != null)
		 * preparedStatement.close();
		 * 
		 * } catch (Exception e2) { // TODO: handle exception }
		 * 
		 * }
		 */

	}

	public void write(final String bName,final String bTitle,final String bContent) {
		
		template.update(new PreparedStatementCreator(){
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				String query = "insert into ggikko_board (bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) values(ggikko_board_seq.nextval, ?,?,?,0, ggikko_board_seq.currval, 0,0)";
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt.setString(1, bName);
				pstmt.setString(2, bTitle);
				pstmt.setString(3, bContent);

				return pstmt;
			}
		} );

		/*Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = dataSource.getConnection();
			String query = "insert into ggikko_board (bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) values(ggikko_board_seq.nextval, ?,?,?,0, ggikko_board_seq.currval, 0,0)";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, bName);
			preparedStatement.setString(2, bTitle);
			preparedStatement.setString(3, bContent);

			int m = preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (connection != null)
					connection.close();
				if (preparedStatement != null)
					preparedStatement.close();

			} catch (Exception e2) {
				e2.printStackTrace();
			}*/
		

	}

	public BDto contentView(String strId) {

		upHit(strId);

		String query = "select * from ggikko_board where bId =" + strId;
		return template.queryForObject(query, new BeanPropertyRowMapper<BDto>(
				BDto.class));

		/* return dto; */

		/*
		 * BDto dto = null; Connection connection = null; PreparedStatement
		 * preparedStatement = null; ResultSet resultSet = null;
		 * 
		 * try { connection = dataSource.getConnection(); String query =
		 * "select * from ggikko_board where bId = ?"; preparedStatement =
		 * connection.prepareStatement(query); preparedStatement.setInt(1,
		 * Integer.parseInt(strId)); resultSet =
		 * preparedStatement.executeQuery();
		 * 
		 * if (resultSet.next()) { int bId = resultSet.getInt("bId"); String
		 * bName = resultSet.getString("bName"); String bTitle =
		 * resultSet.getString("bTitle"); String bContent =
		 * resultSet.getString("bContent"); Timestamp bDate =
		 * resultSet.getTimestamp("bDate"); int bHit = resultSet.getInt("bHit");
		 * int bGroup = resultSet.getInt("bGroup"); int bStep =
		 * resultSet.getInt("bStep"); int bIndent = resultSet.getInt("bIndent");
		 * 
		 * dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup,
		 * bStep, bIndent);
		 * 
		 * }
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } finally {
		 * 
		 * try { if (resultSet != null) resultSet.close(); if (connection !=
		 * null) connection.close(); if (preparedStatement != null)
		 * preparedStatement.close(); } catch (Exception e2) {
		 * e2.printStackTrace(); } }
		 */

	}

	public void modify(final String bId,final String bName,final String bTitle,final String bContent) {
		
		String query = "update ggikko_board set bName = ?, bTitle = ?, bContent = ? where bId =? ";
		
		template.update(query, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, bName);
				ps.setString(2, bTitle);
				ps.setString(3, bContent);
				ps.setInt(4, Integer.parseInt(bId));
				
			}
		});
		

		/*Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();
			String query = "update ggikko_board set bName = ?, bTitle = ?, bContent = ? where bId =? ";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, bName);
			preparedStatement.setString(2, bTitle);
			preparedStatement.setString(3, bContent);
			preparedStatement.setInt(4, Integer.parseInt(bId));

			int m = preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
				if (preparedStatement != null)
					preparedStatement.close();

			} catch (Exception e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}

		}*/

	}
	
	public void delete(final String strId){
		
		String query = "delete from ggikko_board where bId = ?";
		
		template.update(query, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, strId);
				
			}
			
			
		});
		
	}

	public void upHit(final String bId) {

		String query = "update ggikko_board set bHit = bHit + 1 where bid = ?";

		template.update(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, Integer.parseInt(bId));
			}
		});

		/*
		 * Connection connection = null; PreparedStatement preparedStatement =
		 * null;
		 * 
		 * try { connection = dataSource.getConnection(); String query =
		 * "update ggikko_board set bHit = bHit + 1 where bid = ?";
		 * preparedStatement = connection.prepareStatement(query);
		 * preparedStatement.setString(1, bId);
		 * 
		 * int m = preparedStatement.executeUpdate();
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } finally { try { if
		 * (connection != null) connection.close(); if (preparedStatement !=
		 * null) preparedStatement.close();
		 * 
		 * } catch (Exception e2) { e2.printStackTrace(); } } }
		 * 
		 * public void delete(String strID) {
		 * 
		 * Connection connection = null; PreparedStatement preparedStatement =
		 * null;
		 * 
		 * try {
		 * 
		 * connection = dataSource.getConnection(); String query =
		 * "delete from ggikko_board where bId = ?"; preparedStatement =
		 * connection.prepareStatement(query); preparedStatement.setInt(1,
		 * Integer.parseInt(strID)); int m = preparedStatement.executeUpdate();
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } finally {
		 * 
		 * try { if (connection != null) connection.close(); if
		 * (preparedStatement != null) preparedStatement.close();
		 * 
		 * } catch (Exception e2) { e2.printStackTrace(); }
		 * 
		 * }
		 */
	}

	public BDto reply_view(String strId) {

	/*	BDto dto = null;*/
		
		String query = "select * from ggikko_board where bId =" + strId;
		return template.queryForObject(query,  new BeanPropertyRowMapper<BDto>(BDto.class));

	/*	Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			String query = "select * from ggikko_board where bId = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(strID));
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int bId = resultSet.getInt("bId");
				String bName = resultSet.getString("bName");
				String bTitle = resultSet.getString("bTitle");
				String bContent = resultSet.getString("bContent");
				Timestamp bDate = resultSet.getTimestamp("bDate");
				int bHit = resultSet.getInt("bHit");
				int bGroup = resultSet.getInt("bGroup");
				int bStep = resultSet.getInt("bStep");
				int bIndent = resultSet.getInt("bIndent");

				dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit,
						bGroup, bStep, bIndent);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (connection != null)
					connection.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (resultSet != null)
					resultSet.close();

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return dto;
*/
	}

	public void reply(final String bId,final String bName,final String bTitle,final String bContent,
			final String bGroup, final String bStep, final String bIndent) {
		
		String query = "insert into ggikko_board (bId, bName, bTitle, bContent, bGroup, bStep, bIndent) values (ggikko_board_seq.nextval,?,?,?,?,?,?)";
		
		template.update(query, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				
				ps.setString(1, bName);
				ps.setString(2, bTitle);
				ps.setString(3, bContent);
				ps.setInt(4, Integer.parseInt(bGroup));
				ps.setInt(5, Integer.parseInt(bStep) + 1);
				ps.setInt(6, Integer.parseInt(bIndent) + 1);
				
			}
			
			
		});

/*		replyShape(bGroup, bStep);

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = dataSource.getConnection();
			String query = "insert into ggikko_board (bId, bName, bTitle, bContent, bGroup, bStep, bIndent) values (ggikko_board_seq.nextval,?,?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(query);

			preparedStatement.setString(1, bName);
			preparedStatement.setString(2, bTitle);
			preparedStatement.setString(3, bContent);
			preparedStatement.setInt(4, Integer.parseInt(bGroup));
			preparedStatement.setInt(5, Integer.parseInt(bStep) + 1);
			preparedStatement.setInt(6, Integer.parseInt(bIndent) + 1);

			int m = preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();

			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
*/
	}

	private void replyShape(final String strGroup,final String strStep) {
		
		String query = "update ggikko_board set bStep = bStep +1 where bGroup =? and bStep > ? ";
		template.update(query, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				
				ps.setInt(1, Integer.parseInt(strGroup));
				ps.setInt(2, Integer.parseInt(strStep));
				
				
			}
			
			
		});
	}
	
		
	/*	Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();
			String query = "update ggikko_board set bStep = bStep +1 where bGroup =? and bStep > ? ";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(strGroup));
			preparedStatement.setInt(2, Integer.parseInt(strStep));

			int m = preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {

				if (connection != null)
					connection.close();
				if (preparedStatement != null)
					preparedStatement.close();

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}*/

}
