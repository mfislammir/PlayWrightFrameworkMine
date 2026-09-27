package tests;

import com.microsoft.playwright.Locator;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingCancellationTest extends TestBase {

    @Test(groups = {"framework"}, 
          description = "View booking details and cancel booking - Verify record disappears")
    public void testBookingCancellation() {
        // Step 1: Login to application
        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();

        // Step 2: Navigate to My Bookings
        BookingsPage1 bookingsPage = new BookingsPage1(page);
        bookingsPage.goTo();
        bookingsPage.waitForBookingsToLoad();

        // Step 3: Get first booking ID and store initial count of bookings
        Locator firstBookingLink = page.locator("a[href*=\"/bookings/\"]").first();
        String bookingUrl = firstBookingLink.getAttribute("href");
        String firstBookingId = bookingUrl.split("/")[bookingUrl.split("/").length - 1];
        int initialBookingCount = page.locator("a[href*=\"/bookings/\"]").count();

        // Step 4: Click View Details on first booking
        BookingDetailsPage1 bookingDetailsPage = bookingsPage.viewBookingDetails(firstBookingId);

        // Step 5: Verify booking details page is displayed
        bookingDetailsPage.verifyBookingDetailsPageIsDisplayed();

        // Step 6: Cancel the booking
        bookingDetailsPage.cancelBooking();

        // Step 7: Verify cancellation success message appears and redirects to bookings page
        bookingDetailsPage.verifyBookingCancellationSuccess();

        // Step 8: Verify the booking count decreased by 1
        int finalBookingCount = page.locator("a[href*=\"/bookings/\"]").count();
        Assert.assertEquals(finalBookingCount, initialBookingCount,
            "Booking count should decrease by 1 after cancellation");

        // Step 9: Verify the cancelled booking ID no longer appears in the list
        assertThat(page.locator("a[href=\"/bookings/" + firstBookingId + "\"]")).not().isVisible();
    }

    @AfterMethod
    public void tearDown() {
        // Cleanup if needed
    }

}
