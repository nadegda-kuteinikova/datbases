package com.greenhouse.service;

import com.greenhouse.dao.*;
import com.greenhouse.model.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class CrudDemoService {

    private final SupplierDao supplierDao = new SupplierDao();
    private final SeedCultureDao seedCultureDao = new SeedCultureDao();
    private final RackDao rackDao = new RackDao();
    private final SeedBatchDao seedBatchDao = new SeedBatchDao();
    private final CellDao cellDao = new CellDao();
    private final SensorDao sensorDao = new SensorDao();
    private final ChangeHistoryDao changeHistoryDao = new ChangeHistoryDao();

    // CREATE

    public void demoCreate() throws SQLException {

        System.out.println("=== CREATE ===");

        Supplier supplier = new Supplier(null, "Тестовый поставщик", "+79999999999", 4.8);

        int supplierId = supplierDao.insert(supplier);

        System.out.printf("Создан поставщик: id=%d, %s%n", supplierId, supplier.getCompanyName());

        SeedCulture culture = new SeedCulture(null, "Температура 20-25°C", "Тестовый сорт", "Томат");

        int cultureId = seedCultureDao.insert(culture);

        System.out.printf("Создана культура: id=%d, %s%n", cultureId, culture.getName());

        Rack rack = new Rack(null, LocalDate.now(), 3, "Тестовый стеллаж");

        short rackId = rackDao.insert(rack);

        System.out.printf("Создан стеллаж: id=%d%n", rackId);

        System.out.println();
    }

    // READ

    public void demoRead() throws SQLException {

        System.out.println("=== READ ===");

        System.out.println("\nПоставщики:");

        for (Supplier supplier : supplierDao.findAll()) {
            System.out.println(supplier);
        }

        System.out.println("\nКультуры:");

        for (SeedCulture culture : seedCultureDao.findAll()) {
            System.out.println(culture);
        }

        System.out.println("\nСтеллажи:");

        for (Rack rack : rackDao.findAll()) {
            System.out.println(rack);
        }

        System.out.println("\nПоиск поставщика id=1");

        supplierDao.findById(1).ifPresentOrElse(System.out::println, () -> System.out.println("Не найден"));

        System.out.println();
    }

    // UPDATE

    public void demoUpdate() throws SQLException {

        System.out.println("=== UPDATE ===");

        supplierDao.findById(1).ifPresent(supplier -> {

            double oldRating = supplier.getRating();

            supplier.setRating(supplier.getRating() + 0.1);

            try {

                boolean updated = supplierDao.update(supplier);

                System.out.printf("Рейтинг %.2f -> %.2f (успех=%b)%n", oldRating, supplier.getRating(), updated);

            } catch (SQLException e) {

                System.out.println(e.getMessage());
            }
        });

        seedCultureDao.findById(1).ifPresent(culture -> {

            String oldName = culture.getName();

            culture.setName(oldName + " TEST");

            try {

                boolean updated = seedCultureDao.update(culture);

                System.out.printf("%s -> %s (успех=%b)%n", oldName, culture.getName(), updated);

            } catch (SQLException e) {

                System.out.println(e.getMessage());
            }
        });

        System.out.println();
    }

    // DELETE

    public void demoDelete() throws SQLException {

        System.out.println("=== DELETE ===");

        Supplier supplier = new Supplier(null, "Удалить меня", "delete@test.ru", 1.0);

        int id = supplierDao.insert(supplier);

        System.out.printf("Создан поставщик id=%d%n", id);

        boolean deleted = supplierDao.delete(id);

        System.out.printf("Удалён id=%d (успех=%b)%n", id, deleted);

        System.out.println();
    }

    // BATCH INSERT

    public void demoBatchInsert() throws SQLException {

        System.out.println("=== BATCH INSERT ===");

        Rack rack = new Rack(null, LocalDate.now(), 2, "Batch Rack");

        short rackId = rackDao.insert(rack);

        List<Cell> cells = new ArrayList<>();

        for (int i = 0; i < 20; i++) {

            cells.add(new Cell(null, (int) rackId, null, null));
        }

        long start = System.nanoTime();

        int inserted = cellDao.batchInsert(cells);

        long elapsed = (System.nanoTime() - start) / 1_000_000;

        System.out.printf("Добавлено %d ячеек за %d мс%n", inserted, elapsed);

        rackDao.delete(rackId);

        System.out.println();
    }

    // TRANSACTION DEMO

    public void demoTransaction() throws SQLException {

        System.out.println("=== TRANSACTION DEMO ===");

        Cell cell = new Cell(null, 1, null, LocalDate.now());

        int cellId = cellDao.insert(cell);

        Sensor sensor = new Sensor(null, cellId, "температура", LocalDate.now());

        int sensorId = sensorDao.insert(sensor);

        ChangeHistory history = new ChangeHistory(null, sensorId, BigDecimal.valueOf(23.5), OffsetDateTime.now());

        long historyId = changeHistoryDao.insert(history);

        System.out.printf("Создан датчик id=%d%n", sensorId);

        System.out.printf("Создана запись истории id=%d%n", historyId);

        System.out.println();
    }

    public static String truncate(String value, int max) {

        if (value == null) {
            return "";
        }

        return value.length() > max ? value.substring(0, max - 1) + "…" : value;
    }
}