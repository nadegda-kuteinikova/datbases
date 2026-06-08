package com.greenhouse.service;

import com.greenhouse.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BusinessQueryService {

    // 1. Партии с всхожестью выше средней

    public void aboveAverageGermination() throws SQLException {

        System.out.println("\n=== Партии с всхожестью выше средней ===");

        String sql = """
                SELECT *
                FROM greenhouse.seed_batches
                WHERE germination_percent >
                (
                    SELECT AVG(germination_percent)
                    FROM greenhouse.seed_batches
                )
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.printf(
                        "ID=%d | Culture=%d | Supplier=%d | %.2f%%%n",
                        rs.getInt("id"),
                        rs.getInt("culture_id"),
                        rs.getInt("supplier_id"),
                        rs.getDouble("germination_percent")
                );
            }
        }
    }

    // 2. Поставщики, имеющие поставки

    public void suppliersWithDeliveries() throws SQLException {

        System.out.println("\n=== Поставщики с поставками ===");

        String sql = """
                SELECT
                    s.id,
                    s.company_name,
                    s.rating
                FROM greenhouse.suppliers s
                WHERE EXISTS
                (
                    SELECT 1
                    FROM greenhouse.seed_batches sb
                    WHERE sb.supplier_id = s.id
                )
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.printf(
                        "%d | %s | %.2f%n",
                        rs.getInt("id"),
                        rs.getString("company_name"),
                        rs.getDouble("rating")
                );
            }
        }
    }

    // 3. Свободные ячейки

    public void emptyCells() throws SQLException {

        System.out.println("\n=== Свободные ячейки ===");

        String sql = """
                SELECT *
                FROM greenhouse.cells
                WHERE current_culture IS NULL
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.printf(
                        "Cell ID=%d | Rack=%d%n",
                        rs.getInt("id"),
                        rs.getInt("rack_id")
                );
            }
        }
    }

    // 4. Стеллажи без ячеек

    public void racksWithoutCells() throws SQLException {

        System.out.println("\n=== Стеллажи без ячеек ===");

        String sql = """
                SELECT
                    r.id,
                    r.name
                FROM greenhouse.racks r
                LEFT JOIN greenhouse.cells c
                ON r.id = c.rack_id
                WHERE c.id IS NULL
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.printf(
                        "%d | %s%n",
                        rs.getInt("id"),
                        rs.getString("name")
                );
            }
        }
    }

    // 5. Культуры и поставщики

    public void culturesBySupplier() throws SQLException {

        System.out.println("\n=== Культуры и поставщики ===");

        String sql = """
                SELECT
                    sb.id,
                    sc.name,
                    sc.variety,
                    s.company_name,
                    sb.germination_percent
                FROM greenhouse.seed_batches sb
                INNER JOIN greenhouse.seed_cultures sc
                    ON sb.culture_id = sc.id
                INNER JOIN greenhouse.suppliers s
                    ON sb.supplier_id = s.id
                ORDER BY sb.germination_percent DESC
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.printf(
                        "%d | %s | %s | %s | %.2f%%%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("variety"),
                        rs.getString("company_name"),
                        rs.getDouble("germination_percent")
                );
            }
        }
    }

    // 6. История показаний датчиков

    public void sensorHistory() throws SQLException {

        System.out.println("\n=== История показаний датчиков ===");

        String sql = """
                SELECT
                    ch.id,
                    s.sensor_type,
                    ch.value,
                    ch.recorded_at
                FROM greenhouse.change_history ch
                INNER JOIN greenhouse.sensors s
                    ON ch.sensor_id = s.id
                ORDER BY ch.recorded_at DESC
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.printf(
                        "%d | %s | %.2f | %s%n",
                        rs.getLong("id"),
                        rs.getString("sensor_type"),
                        rs.getDouble("value"),
                        rs.getObject("recorded_at")
                );
            }
        }
    }

    // 7. Полная информация о теплице

    public void fullGreenhouseInfo() throws SQLException {

        System.out.println("\n=== Полная информация о теплице ===");

        String sql = """
                SELECT
                    c.id AS cell_id,
                    r.name AS rack_name,
                    sc.name AS culture_name,
                    sc.variety,
                    s.company_name,
                    sb.germination_percent,
                    c.planting_date
                FROM greenhouse.cells c
                LEFT JOIN greenhouse.seed_cultures sc
                    ON c.current_culture = sc.id
                LEFT JOIN greenhouse.seed_batches sb
                    ON sc.id = sb.culture_id
                LEFT JOIN greenhouse.suppliers s
                    ON sb.supplier_id = s.id
                INNER JOIN greenhouse.racks r
                    ON c.rack_id = r.id
                ORDER BY c.id
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.printf(
                        "Cell=%d | Rack=%s | Culture=%s | Variety=%s | Supplier=%s | Germination=%.2f%%%n",
                        rs.getInt("cell_id"),
                        rs.getString("rack_name"),
                        rs.getString("culture_name"),
                        rs.getString("variety"),
                        rs.getString("company_name"),
                        rs.getDouble("germination_percent")
                );
            }
        }
    }
}
