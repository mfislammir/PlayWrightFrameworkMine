package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicTest {


    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage(); //browser.newContext();
        page.setDefaultTimeout(8000);
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        PlaywrightAssertions.setDefaultAssertionTimeout(7000); //Global Assertion time out 7 seconds

    }

    @Test(description = "Create Event -Book that event and verify if its booked")
    public void DemoTest() throws InterruptedException {
        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");

        page.getByPlaceholder("you@email.com").fill("fokrulislambd@gmail.com");
        page.getByLabel("Password").fill("Iamking123@");
        //locator
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();
       // step 1

       // page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Explore All Events")).click();
       // Thread.sleep(2000);
       // page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Add New Event")).click();
        page .navigate("https://eventhub.rahulshettyacademy.com/admin/events");

        page.locator("//input[@id='event-title-input']").fill("Mir Academy",new Locator.FillOptions().setTimeout(10000));
        // textarea[placeholder='Describe the event…']
        page.locator("textarea[placeholder='Describe the event…']").fill("Fokrul Islam's QA Meetups");
        page.getByLabel("Category").selectOption("Conference");
        page.getByLabel("City").fill("New York");
        page.getByLabel("Venue").fill("QA Test Venue");
        page.getByLabel("Event Date & Time").fill("2026-10-13T19:02");
        page.getByLabel("Price ($)").fill("100");
        page.getByLabel("Total Seats").fill("50");
        page.locator("#add-event-btn").click(new Locator.ClickOptions().setTimeout(12000));

        //Event created! 2-3
        assertThat(page.getByText("Event created!")).isVisible(); //5 seconds -Assertion

        //Step 2 - Find newly created event in the events page
        page.locator("#nav-events").click();
        Locator eventCards = page.getByTestId("event-card"); //{loc1,loc2,loc3,loc4}

        System.out.println(eventCards.count());

        //Visibility of the card which we have added
        Locator targetCard =  eventCards.filter(new Locator.FilterOptions().setHasText("Mir Academy"));

        assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));

        String seatsText =  targetCard.getByText("seats").innerText();
        System.out.println(seatsText);
        int seatsNumBeforeBooking =Integer.parseInt(seatsText.split(" ")[0]);
        targetCard.getByTestId("book-now-btn").click();


        page.getByLabel("Full Name").fill("QA Student");
        page.locator("#customer-email").fill("test.student@example.com");
        page.getByPlaceholder("+91 98765 43210").fill("1233356600");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingRef = page.locator(".booking-ref").innerText();

        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();

        // Verify in Booking History

        Locator bookingCards = page.locator("#booking-card"); //{local1,loc2,loc3}
        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(targetBookingCard).isVisible();
        // seat count reduction check
        page.locator("#nav-events").click();
        page.waitForTimeout(1000);

        Locator eventCardsAfterBooking = page.getByTestId("event-card"); //{loc1,loc2,loc3,loc4}
        //Visibility of the card which we have added
        Locator targetCardAfterBooking =  eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("Mir Academy"));
        String seatsTextAfterBooking =  targetCardAfterBooking.getByText("seats").innerText();
        System.out.println(seatsTextAfterBooking);
        //Afterbookings < BeforeBookings
        //  46 seats available
        int seatsNumAfterBooking =Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);

        Assert.assertTrue(seatsNumBeforeBooking > seatsNumAfterBooking);
        //locators
        Thread.sleep(5000);
    }


    @AfterMethod
    public void tearDown() {

    page.close();
    }
}
