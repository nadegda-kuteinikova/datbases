package com.greenhouse;

import com.greenhouse.db.ConnectionManager;
import com.greenhouse.db.SchemaInitializer;
import com.greenhouse.service.BusinessQueryService;
import com.greenhouse.service.CrudDemoService;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static final CrudDemoService crudDemo =
            new CrudDemoService();

    private static final BusinessQueryService businessQuery =
            new BusinessQueryService();

    public static void main(String[] args) {

        System.out.println(
                "=== JDBC Greenhouse Demo ==="
        );

        System.out.println(
                "База данных готова.\n"
        );

        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {

            System.out.print("""
                    
                    [1] CRUD
                    [2] Бизнес-запросы
                    [3] Всё
                    [0] Выход
                    
                    >
                    """);

            try {

                switch (scanner.nextLine().trim()) {

                    case "1" ->
                            runCrudMenu(scanner);

                    case "2" ->
                            runBusinessMenu(scanner);

                    case "3" ->
                            runAllDemo();

                    case "0" ->
                            running = false;

                    default ->
                            System.out.println(
                                    "Неверный выбор"
                            );
                }

            } catch (SQLException e) {

                System.err.println(
                        "SQL Error: "
                                + e.getMessage()
                );
            }
        }

        System.out.println(
                "\nДо свидания!"
        );

        ConnectionManager.close();
    }

    // CRUD

    private static void runCrudMenu(
            Scanner scanner
    ) throws SQLException {

        while (true) {

            System.out.print("""
                    
                    [1] Create
                    [2] Read
                    [3] Update
                    [4] Delete
                    [5] Batch Insert
                    [6] Transaction
                    [7] Всё
                    [0] Назад
                    
                    >
                    """);

            switch (scanner.nextLine().trim()) {

                case "1" ->
                        crudDemo.demoCreate();

                case "2" ->
                        crudDemo.demoRead();

                case "3" ->
                        crudDemo.demoUpdate();

                case "4" ->
                        crudDemo.demoDelete();

                case "5" ->
                        crudDemo.demoBatchInsert();

                case "6" ->
                        crudDemo.demoTransaction();

                case "7" ->
                        runAllCrud();

                case "0" -> {
                    return;
                }

                default ->
                        System.out.println(
                                "Неверный выбор"
                        );
            }
        }
    }

    private static void runAllCrud()
            throws SQLException {

        crudDemo.demoCreate();
        crudDemo.demoRead();
        crudDemo.demoUpdate();
        crudDemo.demoDelete();
        crudDemo.demoBatchInsert();
        crudDemo.demoTransaction();
    }

    // BUSINESS

    private static void runBusinessMenu(
            Scanner scanner
    ) throws SQLException {

        while (true) {

            System.out.print("""
                    
                    [1] Всхожесть выше средней
                    [2] Поставщики с поставками
                    [3] Свободные ячейки
                    [4] Стеллажи без ячеек
                    [5] Культуры и поставщики
                    [6] История датчиков
                    [7] Полная информация
                    [8] Всё
                    [0] Назад
                    
                    >
                    """);

            switch (scanner.nextLine().trim()) {

                case "1" ->
                        businessQuery.aboveAverageGermination();

                case "2" ->
                        businessQuery.suppliersWithDeliveries();

                case "3" ->
                        businessQuery.emptyCells();

                case "4" ->
                        businessQuery.racksWithoutCells();

                case "5" ->
                        businessQuery.culturesBySupplier();

                case "6" ->
                        businessQuery.sensorHistory();

                case "7" ->
                        businessQuery.fullGreenhouseInfo();

                case "8" ->
                        runAllBusinessQueries();

                case "0" -> {
                    return;
                }

                default ->
                        System.out.println(
                                "Неверный выбор"
                        );
            }
        }
    }

    private static void runAllBusinessQueries()
            throws SQLException {

        businessQuery.aboveAverageGermination();

        businessQuery.suppliersWithDeliveries();

        businessQuery.emptyCells();

        businessQuery.racksWithoutCells();

        businessQuery.culturesBySupplier();

        businessQuery.sensorHistory();

        businessQuery.fullGreenhouseInfo();
    }

    // RUN ALL

    private static void runAllDemo()
            throws SQLException {

        System.out.println(
                "\n=== CRUD ===\n"
        );

        runAllCrud();

        System.out.println(
                "\n=== BUSINESS ===\n"
        );

        runAllBusinessQueries();

        System.out.println(
                "\nДемонстрация завершена."
        );
    }
}
