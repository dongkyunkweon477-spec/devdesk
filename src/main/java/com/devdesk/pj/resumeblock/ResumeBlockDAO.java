package com.devdesk.pj.resumeblock;

import com.devdesk.pj.main.DBManager_new;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResumeBlockDAO {

    // ── 블록 전체 조회 (회원별) ──
    public List<ResumeBlockVO> selectAllBlocks(int memberId) {
        List<ResumeBlockVO> list = new ArrayList<>();
        String sql = "SELECT b.*, " +
                "(SELECT NVL(MAX(version_num),1) FROM resume_block_version WHERE block_id = b.block_id) AS latest_version " +
                "FROM resume_block b " +
                "WHERE b.member_id = ? " +
                "ORDER BY b.is_star DESC, b.updated_date DESC";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ResumeBlockVO vo = new ResumeBlockVO();
                vo.setBlockId(rs.getInt("block_id"));
                vo.setMemberId(rs.getInt("member_id"));
                vo.setCategoryId(rs.getString("category_id"));
                vo.setTitle(rs.getString("title"));
                vo.setContent(rs.getString("content"));
                vo.setTags(rs.getString("tags"));
                vo.setIsStar(rs.getInt("is_star"));
                vo.setCharLimit(rs.getInt("char_limit"));
                vo.setCreatedDate(rs.getString("created_date"));
                vo.setUpdatedDate(rs.getString("updated_date"));
                vo.setLatestVersion(rs.getInt("latest_version"));
                list.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }
        return list;
    }

    // ── 블록 카테고리별 조회 ──
    public List<ResumeBlockVO> selectBlocksByCategory(int memberId, String categoryId) {
        List<ResumeBlockVO> list = new ArrayList<>();
        String sql = "SELECT b.*, " +
                "(SELECT NVL(MAX(version_num),1) FROM resume_block_version WHERE block_id = b.block_id) AS latest_version " +
                "FROM resume_block b " +
                "WHERE b.member_id = ? AND b.category_id = ? " +
                "ORDER BY b.is_star DESC, b.updated_date DESC";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            pstmt.setString(2, categoryId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ResumeBlockVO vo = new ResumeBlockVO();
                vo.setBlockId(rs.getInt("block_id"));
                vo.setMemberId(rs.getInt("member_id"));
                vo.setCategoryId(rs.getString("category_id"));
                vo.setTitle(rs.getString("title"));
                vo.setContent(rs.getString("content"));
                vo.setTags(rs.getString("tags"));
                vo.setIsStar(rs.getInt("is_star"));
                vo.setCharLimit(rs.getInt("char_limit"));
                vo.setCreatedDate(rs.getString("created_date"));
                vo.setUpdatedDate(rs.getString("updated_date"));
                vo.setLatestVersion(rs.getInt("latest_version"));
                list.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }
        return list;
    }

    // ── 블록 단건 조회 ──
    public ResumeBlockVO selectBlock(int blockId) {
        String sql = "SELECT b.*, " +
                "(SELECT NVL(MAX(version_num),1) FROM resume_block_version WHERE block_id = b.block_id) AS latest_version " +
                "FROM resume_block b WHERE b.block_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, blockId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                ResumeBlockVO vo = new ResumeBlockVO();
                vo.setBlockId(rs.getInt("block_id"));
                vo.setMemberId(rs.getInt("member_id"));
                vo.setCategoryId(rs.getString("category_id"));
                vo.setTitle(rs.getString("title"));
                vo.setContent(rs.getString("content"));
                vo.setTags(rs.getString("tags"));
                vo.setIsStar(rs.getInt("is_star"));
                vo.setCharLimit(rs.getInt("char_limit"));
                vo.setCreatedDate(rs.getString("created_date"));
                vo.setUpdatedDate(rs.getString("updated_date"));
                vo.setLatestVersion(rs.getInt("latest_version"));
                return vo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }
        return null;
    }

    // ── 블록 추가 (+ 첫 번째 버전 자동 생성) ──
    public int insertBlock(int memberId, String categoryId, String title,
                           String content, String tags, int charLimit) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int blockId = -1;

        String sqlBlock = "INSERT INTO resume_block " +
                "(block_id, member_id, category_id, title, content, tags, char_limit) " +
                "VALUES (resume_block_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        String sqlGetId = "SELECT resume_block_seq.CURRVAL FROM dual";

        String sqlVersion = "INSERT INTO resume_block_version " +
                "(version_id, block_id, version_num, content) " +
                "VALUES (resume_block_version_seq.NEXTVAL, ?, 1, ?)";

        try {
            con = DBManager_new.connect();
            con.setAutoCommit(false);

            // 블록 INSERT
            pstmt = con.prepareStatement(sqlBlock);
            pstmt.setInt(1, memberId);
            pstmt.setString(2, categoryId);
            pstmt.setString(3, title);
            pstmt.setString(4, content);
            pstmt.setString(5, tags);
            pstmt.setInt(6, charLimit);
            pstmt.executeUpdate();
            pstmt.close();

            // 생성된 block_id 가져오기
            pstmt = con.prepareStatement(sqlGetId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                blockId = rs.getInt(1);
            }
            rs.close();
            pstmt.close();

            // 첫 버전 INSERT
            pstmt = con.prepareStatement(sqlVersion);
            pstmt.setInt(1, blockId);
            pstmt.setString(2, content);
            pstmt.executeUpdate();

            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (Exception ex) { ex.printStackTrace(); }
            DBManager_new.close(con, pstmt, null);
        }
        return blockId;
    }

    // ── 블록 수정 (내용 변경 시 새 버전 자동 생성) ──
    public void updateBlock(int blockId, String categoryId, String title,
                            String content, String tags, int charLimit) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sqlGetOld = "SELECT content FROM resume_block WHERE block_id = ?";
        String sqlUpdate = "UPDATE resume_block SET category_id = ?, title = ?, " +
                "content = ?, tags = ?, char_limit = ?, updated_date = SYSDATE WHERE block_id = ?";
        String sqlMaxVer = "SELECT NVL(MAX(version_num), 0) FROM resume_block_version WHERE block_id = ?";
        String sqlVersion = "INSERT INTO resume_block_version " +
                "(version_id, block_id, version_num, content) " +
                "VALUES (resume_block_version_seq.NEXTVAL, ?, ?, ?)";

        try {
            con = DBManager_new.connect();
            con.setAutoCommit(false);

            // 기존 내용 조회
            pstmt = con.prepareStatement(sqlGetOld);
            pstmt.setInt(1, blockId);
            rs = pstmt.executeQuery();
            String oldContent = "";
            if (rs.next()) {
                oldContent = rs.getString("content");
            }
            rs.close();
            pstmt.close();

            // 블록 UPDATE
            pstmt = con.prepareStatement(sqlUpdate);
            pstmt.setString(1, categoryId);
            pstmt.setString(2, title);
            pstmt.setString(3, content);
            pstmt.setString(4, tags);
            pstmt.setInt(5, charLimit);
            pstmt.setInt(6, blockId);
            pstmt.executeUpdate();
            pstmt.close();

            // 내용이 바뀌었으면 새 버전 추가
            if (!content.equals(oldContent)) {
                pstmt = con.prepareStatement(sqlMaxVer);
                pstmt.setInt(1, blockId);
                rs = pstmt.executeQuery();
                int maxVer = 0;
                if (rs.next()) {
                    maxVer = rs.getInt(1);
                }
                rs.close();
                pstmt.close();

                pstmt = con.prepareStatement(sqlVersion);
                pstmt.setInt(1, blockId);
                pstmt.setInt(2, maxVer + 1);
                pstmt.setString(3, content);
                pstmt.executeUpdate();
            }

            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (Exception ex) { ex.printStackTrace(); }
            DBManager_new.close(con, pstmt, null);
        }
    }

    // ── 블록 삭제 (CASCADE로 버전도 삭제) ──
    public void deleteBlock(int blockId) {
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM resume_block WHERE block_id = ?";

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, blockId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, null);
        }
    }

    // ── 즐겨찾기 토글 ──
    public void toggleStar(int blockId) {
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "UPDATE resume_block SET is_star = CASE WHEN is_star = 1 THEN 0 ELSE 1 END, " +
                "updated_date = SYSDATE WHERE block_id = ?";

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, blockId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, null);
        }
    }

    // ── 버전 히스토리 조회 ──
    public List<ResumeBlockVersionVO> selectVersions(int blockId) {
        List<ResumeBlockVersionVO> list = new ArrayList<>();
        String sql = "SELECT * FROM resume_block_version WHERE block_id = ? ORDER BY version_num DESC";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, blockId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ResumeBlockVersionVO vo = new ResumeBlockVersionVO();
                vo.setVersionId(rs.getInt("version_id"));
                vo.setBlockId(rs.getInt("block_id"));
                vo.setVersionNum(rs.getInt("version_num"));
                vo.setContent(rs.getString("content"));
                vo.setCreatedDate(rs.getString("created_date"));
                list.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }
        return list;
    }

    // ── 즐겨찾기 블록만 조회 ──
    public List<ResumeBlockVO> selectStarBlocks(int memberId) {
        List<ResumeBlockVO> list = new ArrayList<>();
        String sql = "SELECT b.*, " +
                "(SELECT NVL(MAX(version_num),1) FROM resume_block_version WHERE block_id = b.block_id) AS latest_version " +
                "FROM resume_block b " +
                "WHERE b.member_id = ? AND b.is_star = 1 " +
                "ORDER BY b.updated_date DESC";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ResumeBlockVO vo = new ResumeBlockVO();
                vo.setBlockId(rs.getInt("block_id"));
                vo.setMemberId(rs.getInt("member_id"));
                vo.setCategoryId(rs.getString("category_id"));
                vo.setTitle(rs.getString("title"));
                vo.setContent(rs.getString("content"));
                vo.setTags(rs.getString("tags"));
                vo.setIsStar(rs.getInt("is_star"));
                vo.setCharLimit(rs.getInt("char_limit"));
                vo.setCreatedDate(rs.getString("created_date"));
                vo.setUpdatedDate(rs.getString("updated_date"));
                vo.setLatestVersion(rs.getInt("latest_version"));
                list.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }
        return list;
    }

    // ── 통계: 카테고리별 블록 수 ──
    public int countBlocks(int memberId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement("SELECT COUNT(*) FROM resume_block WHERE member_id = ?");
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }
        return count;
    }

    public int countStarBlocks(int memberId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement("SELECT COUNT(*) FROM resume_block WHERE member_id = ? AND is_star = 1");
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }
        return count;
    }

    public int countCategories(int memberId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(
                    "SELECT COUNT(DISTINCT category_id) FROM resume_block WHERE member_id = ?");
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }
        return count;
    }
}
