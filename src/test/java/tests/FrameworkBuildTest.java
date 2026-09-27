package tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertTrue;


public class FrameworkBuildTest extends TestBase{

//Invoke browser ->Invoke tab/page->type url
    //HeadMode- Headless


    @Test(groups = {"framework"},description = "Create Event -Book that event and verify if its booked")
    public void DemoTest() throws InterruptedException {
        String eventTitle = "Mir Academy Test Events";

        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();
        dashboardPage.clickOnExploreAllEvents();
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.clickOnAddNewEvent();
        Thread.sleep(4000);
        //  adminEventsPage.goTo();
        //Step 1 - Create Event from Admin pageeventTitle
        adminEventsPage.createEvent(
                eventTitle,
                "Playwright test event",
                "New York City",
                "Test Venue",
                "2027-12-31T10:00",
                "100",
                "75"
        );
        assertThat(page.getByText("Event created!")).isVisible(); //5 seconds -Assertion

        // Step 2 - Find newly created event in the events page
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        Locator targetCard = eventsPage.findEventCard(eventTitle);
        int seatsNumBeforeBooking = eventsPage.getSeatsCount(targetCard);
        BookingFormPage bookingFormPage = eventsPage.proceedToBookingEvent(targetCard);
        bookingFormPage.fillAndConfirm("QA Student",
                "test.student@example.com",
                "5716543210");
       String bookingReferanceFromEventsPage= eventsPage.bookingReferance();
        BookingsPage1 bookingpage= new BookingsPage1(page);
       // page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();
        bookingpage.goTo();
        bookingpage.bookingVisibility(bookingReferanceFromEventsPage);
        Thread.sleep(2000);
       Locator targetCardAfterBooking = eventsPage.findEventCard(eventTitle);
        int seatsTextAfterBooking= eventsPage.getSeatsCount(targetCardAfterBooking);

       // bookingpage.isBookingVisible(bookingReferanceFromEventsPage);

        Assert.assertTrue(seatsNumBeforeBooking> seatsTextAfterBooking);
        Thread.sleep(4000);

    }








}
