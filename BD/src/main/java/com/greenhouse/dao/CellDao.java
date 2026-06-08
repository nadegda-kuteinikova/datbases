package com.greenhouse.dao;

import com.greenhouse.db.ConnectionManager;
import com.greenhouse.model.Cell;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CellDao {

    public List<Cell> findAll() throws SQLException {

        String sql = """
                SELECT id,
                       rack_id,
                       current_culture,
                       planting_date
                FROM greenhouse.cells
                ORDER BY id
                """;

        List<Cell> result = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }

        return result;
    }

    public Optional<Cell> findById(int id) throws SQLException {

        String sql = """
                SELECT id,
                       rack_id,
                       current_culture,
                       planting_date
                FROM greenhouse.cells
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        ? Optional.of(mapRow(rs))
                        : Optional.empty();
            }
        }
    }

    public int insert(Cell cell) throws SQLException {

        String sql = """
                INSERT INTO greenhouse.cells
                (
                    rack_id,
                    current_culture,
                    planting_date
                )
                VALUES (?, ?, ?)
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, cell.getRackId());

            if (cell.getCurrentCulture() == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, cell.getCurrentCulture());
            }

            if (cell.getPlantingDate() == null) {
                ps.setNull(3, Types.DATE);
            } else {
                ps.setDate(3, Date.valueOf(cell.getPlantingDate()));
            }

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) {

                    int id = keys.getInt(1);

                    cell.setId(id);

                    return id;
                }
            }

            throw new SQLException("Failed to get generated key");
        }
    }

    public boolean update(Cell cell) throws SQLException {

        String sql = """
                UPDATE greenhouse.cells
                SET rack_id = ?,
                    current_culture = ?,
                    planting_date = ?
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cell.getRackId());

            if (cell.getCurrentCulture() == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, cell.getCurrentCulture());
            }

            if (cell.getPlantingDate() == null) {
                ps.setNull(3, Types.DATE);
            } else {
                ps.setDate(3, Date.valueOf(cell.getPlantingDate()));
            }

            ps.setInt(4, cell.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {

        String sql = """
                DELETE FROM greenhouse.cells
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    public int batchInsert(List<Cell> cells) throws SQLException {

        String sql = """
                INSERT INTO greenhouse.cells
                (
                    rack_id,
                    current_culture,
                    planting_date
                )
                VALUES (?, ?, ?)
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Cell cell : cells) {

                ps.setInt(1, cell.getRackId());

                if (cell.getCurrentCulture() == null) {
                    ps.setNull(2, Types.INTEGER);
                } else {
                    ps.setInt(2, cell.getCurrentCulture());
                }

                if (cell.getPlantingDate() == null) {
                    ps.setNull(3, Types.DATE);
                } else {
                    ps.setDate(3, Date.valueOf(cell.getPlantingDate()));
                }

                ps.addBatch();
            }

            return ps.executeBatch().length;
        }
    }

    private Cell mapRow(ResultSet rs) throws SQLException {

        Date plantingDate = rs.getDate("planting_date");

        Integer cultureId = rs.getObject(
                "current_culture",
                Integer.class);

        return new Cell(
                rs.getInt("id"),
                rs.getInt("rack_id"),
                cultureId,
                plantingDate == null
                        ? null
                        : plantingDate.toLocalDate()
        );
    }
}
