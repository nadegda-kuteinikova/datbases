package com.greenhouse.dao;

import com.greenhouse.db.ConnectionManager;
import com.greenhouse.model.SeedCulture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeedCultureDao {

    public List<SeedCulture> findAll()
            throws SQLException {

        String sql = """
                SELECT id,
                       ideal_conditions,
                       variety,
                       name
                FROM greenhouse.seed_cultures
                ORDER BY id
                """;

        List<SeedCulture> result = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }

        return result;
    }

    public Optional<SeedCulture> findById(int id)
            throws SQLException {

        String sql = """
                SELECT id,
                       ideal_conditions,
                       variety,
                       name
                FROM greenhouse.seed_cultures
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next()
                        ? Optional.of(mapRow(rs))
                        : Optional.empty();
            }
        }
    }

    public int insert(SeedCulture culture)
            throws SQLException {

        String sql = """
                INSERT INTO greenhouse.seed_cultures
                (
                    ideal_conditions,
                    variety,
                    name
                )
                VALUES (?, ?, ?)
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1,
                    culture.getIdealConditions());

            ps.setString(2,
                    culture.getVariety());

            ps.setString(3,
                    culture.getName());

            ps.executeUpdate();

            try (ResultSet keys =
                         ps.getGeneratedKeys()) {

                if (keys.next()) {

                    int id = keys.getInt(1);

                    culture.setId(id);

                    return id;
                }
            }

            throw new SQLException(
                    "Failed to get generated key");
        }
    }

    public boolean update(SeedCulture culture)
            throws SQLException {

        String sql = """
                UPDATE greenhouse.seed_cultures
                SET ideal_conditions = ?,
                    variety = ?,
                    name = ?
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1,
                    culture.getIdealConditions());

            ps.setString(2,
                    culture.getVariety());

            ps.setString(3,
                    culture.getName());

            ps.setInt(4,
                    culture.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id)
            throws SQLException {

        String sql = """
                DELETE FROM greenhouse.seed_cultures
                WHERE id = ?
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    private SeedCulture mapRow(ResultSet rs)
            throws SQLException {

        return new SeedCulture(
                rs.getInt("id"),
                rs.getString("ideal_conditions"),
                rs.getString("variety"),
                rs.getString("name")
        );
    }
}