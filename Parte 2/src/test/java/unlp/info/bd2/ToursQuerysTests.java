package unlp.info.bd2;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import unlp.info.bd2.config.AppConfig;
import unlp.info.bd2.model.*;
import unlp.info.bd2.services.ToursService;
import unlp.info.bd2.utils.DBInitializer;
import unlp.info.bd2.utils.ToursException;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@ContextConfiguration(classes = {DBInitializer.class}, loader = AnnotationConfigContextLoader.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ToursQuerysTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    DBInitializer initializer;

    @Autowired
    ToursService service;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @BeforeAll
    public void prepareDB() throws Exception {
        for (String collectionName : mongoTemplate.getCollectionNames()) {
			mongoTemplate.remove(new Query(), collectionName);
		}
        this.initializer.prepareDB();
    }

    @Test
    void init() {

    }

    @Test
    void getAllPurchasesOfUsernameTest() throws ToursException {
        List<Purchase> purchases1 = this.service.getAllPurchasesOfUsername("user1");
        assertEquals(6, purchases1.size());
        this.assertListEquality(purchases1.stream().map(Purchase::getCode).collect(Collectors.toList()), Arrays.asList("P001", "P005","P009", "P011", "P015", "P019"));
        List<Purchase> purchases2 = this.service.getAllPurchasesOfUsername("user3");
        assertEquals(1, purchases2.size());
        this.assertListEquality(purchases2.stream().map(Purchase::getCode).collect(Collectors.toList()), Arrays.asList("P003"));
        List<Purchase> purchases3 = this.service.getAllPurchasesOfUsername("user5");
        assertEquals(0, purchases3.size());
    }

    @Test
    void getUserSpendingMoreThanTest() throws ToursException {
        List<User> users1 = this.service.getUserSpendingMoreThan(500);
        assertEquals(6, users1.size());
        this.assertListEquality(users1.stream().map(User::getUsername).collect(Collectors.toList()), Arrays.asList("user3", "user4", "user6", "user7", "user9", "user10"));
        List<User> users2 = this.service.getUserSpendingMoreThan(800);
        assertEquals(3, users2.size());
        this.assertListEquality(users2.stream().map(User::getUsername).collect(Collectors.toList()), Arrays.asList("user4", "user7", "user10"));
        List<User> users3 = this.service.getUserSpendingMoreThan(1000);
        assertEquals(0, users3.size());
    }

    @Test
    void getUsersWithNumberOfPurchasesTest() throws ToursException {
        List<User> users1 = this.service.getUsersWithNumberOfPurchases(6);
        assertEquals(1, users1.size());
        this.assertListEquality(users1.stream().map(User::getUsername).collect(Collectors.toList()), Arrays.asList("user1"));
        List<User> users2 = this.service.getUsersWithNumberOfPurchases(3);
        assertEquals(2, users2.size());
        this.assertListEquality(users2.stream().map(User::getUsername).collect(Collectors.toList()), Arrays.asList("user1", "user2"));
        List<User> users3 = this.service.getUsersWithNumberOfPurchases(2);
        assertEquals(5, users3.size());
        this.assertListEquality(users3.stream().map(User::getUsername).collect(Collectors.toList()), Arrays.asList("user1", "user2", "user6", "user7", "user10"));
    }

    @Test
    void getTopNSuppliersInPurchasesTest() throws ToursException {
        List<Supplier> suppliers1 = this.service.getTopNSuppliersInPurchases(3);
        assertEquals(3, suppliers1.size());
        this.assertListEquality(suppliers1.stream().map(Supplier::getAuthorizationNumber).collect(Collectors.toList()), Arrays.asList("12345", "54321", "67890"));
        List<Supplier> suppliers2 = this.service.getTopNSuppliersInPurchases(1);
        assertEquals(1, suppliers2.size());
        this.assertListEquality(suppliers2.stream().map(Supplier::getAuthorizationNumber).collect(Collectors.toList()), Arrays.asList("12345"));
    }

    @Test
    void getTopNSuppliersItemsSoldTest() throws ToursException {
        List<Supplier> suppliers1 = this.service.getTopNSuppliersItemsSold(3);
        assertEquals(3, suppliers1.size());
        this.assertListEquality(suppliers1.stream().map(Supplier::getAuthorizationNumber).collect(Collectors.toList()), Arrays.asList("12345", "54321", "67890"));
        List<Supplier> suppliers2 = this.service.getTopNSuppliersItemsSold(1);
        assertEquals(1, suppliers2.size());
        this.assertListEquality(suppliers2.stream().map(Supplier::getAuthorizationNumber).collect(Collectors.toList()), Arrays.asList("12345"));
    }

    @Test
        // DELETE
    void getTop10MoreExpensivePurchasesInServicesTest() throws ToursException {
        List<Purchase> purchases = this.service.getTop10MoreExpensivePurchasesWithServices();
        assertEquals(10, purchases.size());
        this.assertListEquality(purchases.stream().map(Purchase::getCode).collect(Collectors.toList()), Arrays.asList("P004", "P007", "P008", "P010", "P012", "P013", "P014", "P017", "P018", "P020"));
    }

    @Test
    void getTop5UsersMorePurchasesTest() throws ToursException {
        List<User> usersMorePurchases = this.service.getTop5UsersMorePurchases();
        assertEquals(5, usersMorePurchases.size());
        this.assertListEquality(usersMorePurchases.stream().map(User::getUsername).collect(Collectors.toList()), Arrays.asList("user1", "user2", "user6", "user7", "user10"));
    }

    @Test
    void getTop3RoutesWithMoreStopsTest() throws ToursException {
        List<Route> routesWithMoreStops = this.service.getTop3RoutesWithMoreStops();
        assertEquals(3, routesWithMoreStops.size());
        this.assertListEquality(routesWithMoreStops.stream().map(Route::getName).collect(Collectors.toList()), Arrays.asList("City Tour", "Historical Adventure", "Architectural Expedition"));
    }

    @Test
    void getCountOfPurchasesBetweenDatesTest() throws ToursException {
        LocalDate today = LocalDate.now();
        long countOfPurchasesBetweenDates1 = this.service.getCountOfPurchasesBetweenDates(Date.valueOf(today.minusDays(25)), Date.valueOf(today.minusDays(15)));
        assertEquals(6, countOfPurchasesBetweenDates1);
        long countOfPurchasesBetweenDates2 = this.service.getCountOfPurchasesBetweenDates(Date.valueOf(today.minusDays(13)), Date.valueOf(today.minusDays(0)));
        assertEquals(7, countOfPurchasesBetweenDates2);
        long countOfPurchasesBetweenDates3 = this.service.getCountOfPurchasesBetweenDates(Date.valueOf(today.minusDays(26)), Date.valueOf(today.minusDays(22)));
        assertEquals(0, countOfPurchasesBetweenDates3);
    }

    @Test
    void getRoutesWithStopTest() throws ToursException {
        Stop stop1 = this.service.getStopByNameStart("Diagonal Norte").get(0);
        Stop stop2 = this.service.getStopByNameStart("Teatro Colón").get(0);
        Stop stop3 = this.service.getStopByNameStart("La Boca").get(0);

        List<Route> routes1 = this.service.getRoutesWithStop(stop1);
        assertEquals(2, routes1.size());
        this.assertListEquality(routes1.stream().map(Route::getName).collect(Collectors.toList()), List.of("City Tour", "Ruta vacia"));
        List<Route> routes2 = this.service.getRoutesWithStop(stop2);
        assertEquals(2, routes2.size());
        this.assertListEquality(routes2.stream().map(Route::getName).collect(Collectors.toList()), List.of("Historical Adventure", "Architectural Expedition"));
        List<Route> routes3 = this.service.getRoutesWithStop(stop3);
        assertEquals(0, routes3.size());
    }

    @Test
    void getPurchaseWithServiceTest() throws ToursException {
        // DELETE
        Supplier supplier1 = this.service.getSupplierByAuthorizationNumber("54321").get();
        Service service1 = this.service.getServiceByNameAndSupplierId("Delta Coffe", supplier1.getId()).get();
        List<Purchase> purchases1 = this.service.getPurchaseWithService(service1);
        assertEquals(3, purchases1.size());
        this.assertListEquality(purchases1.stream().map(Purchase::getCode).collect(Collectors.toList()), List.of("P001", "P013", "P019"));
    }

    @Test
    void getMaxStopOfRoutesTest() throws ToursException {
        Long maxStopOfRoutes = this.service.getMaxStopOfRoutes();
        assertEquals(9, maxStopOfRoutes);
    }

    @Test
    void getMaxServicesOfSupplierTest() throws ToursException {
        Long maxServicesOfSupplier = this.service.getMaxServicesOfSupplier();
        assertEquals(4, maxServicesOfSupplier);
    }

    @Test
    void getRoutsNotSellTest() throws ToursException {
        // DELETE
        List<Route> routsNotSell = this.service.getRoutsNotSell();
        assertEquals(1, routsNotSell.size());
        this.assertListEquality(routsNotSell.stream().map(Route::getName).collect(Collectors.toList()), List.of("Ruta vacia"));
    }

    @Test
    void getTop3RoutesWithMaxRatingTest() throws ToursException {
        List<Route> routesWithMaxRating = this.service.getTop3RoutesWithMaxAverageRating();
        assertEquals(3, routesWithMaxRating.size());
        this.assertListEquality(routesWithMaxRating.stream().map(Route::getName).collect(Collectors.toList()), List.of("City Tour", "Historical Adventure", "Architectural Expedition"));
    }

    @Test
    void getRoutesWithMinRatingTest() throws ToursException {
        List<Route> routesWithMinRating = this.service.getRoutesWithMinRating();
        assertEquals(2, routesWithMinRating.size());
        this.assertListEquality(routesWithMinRating.stream().map(Route::getName).collect(Collectors.toList()), List.of("Architectural Expedition", "Delta Tour"));
    }

    @Test
    void getMostDemandedServiceTest() throws ToursException {
        Service mostDemandedService = this.service.getMostDemandedService();
        assertEquals("souvenir t-shirt", mostDemandedService.getName());
        assertEquals("I love Buenos Aires t-shirt", mostDemandedService.getDescription());
    }

    @Test
    void getMostBestSellingRouteTest() throws ToursException {
        Route mostBestSellingRoute = this.service.getMostBestSellingRoute();
        assertEquals("City Tour", mostBestSellingRoute.getName());
        assertEquals(9, mostBestSellingRoute.getStops().size());
        assertEquals("Diagonal Norte", mostBestSellingRoute.getStops().get(0).getName());
    }

    @Test
    void getServiceNoAddedToPurchasesTest() throws ToursException {
        List<Service> serviceNoAddedToPurchases = this.service.getServiceNoAddedToPurchases();
        assertEquals(3, serviceNoAddedToPurchases.size());
        this.assertListEquality(serviceNoAddedToPurchases.stream().map(Service::getName).collect(Collectors.toList()), List.of("Architectural Expedition Book", "souvenir retrato", "souvenir retrato 2"));
    }

    @Test
    void getTourGuidesWithRating1Test() throws ToursException {
        List<TourGuideUser> tourGuidesWithRating1 = this.service.getTourGuidesWithRating1();
        assertEquals(2, tourGuidesWithRating1.size());
        this.assertListEquality(tourGuidesWithRating1.stream().map(TourGuideUser::getUsername).collect(Collectors.toList()), List.of("userG3", "userG4"));
    }

    @Test
    void getDriverUserWithMoreRoutesTest() throws ToursException {
        DriverUser driverUserWithMoreRoutes = this.service.getDriverUserWithMoreRoutes();
        assertEquals("userD2", driverUserWithMoreRoutes.getUsername());
    }

    private <T> void assertListEquality(List<T> list1, List<T> list2) {
        if (list1.size() != list2.size()) {
            Assert.fail("Lists have different size");
        }

        for (T objectInList1 : list1) {
            if (!list2.contains(objectInList1)) {
                Assert.fail(objectInList1 + " is not present in list2");
            }
        }
    }
}
