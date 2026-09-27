package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingsPage1 {

    Page page;

    public BookingsPage1(Page page) {
        this.page = page;
    }

    public void goTo() {
        page.navigate("https://eventhub.rahulshettyacademy.com/bookings");
    }

    public BookingDetailsPage1 viewBookingDetails(String bookingId) {
        page.locator("a[href=\"/bookings/" + bookingId + "\"]").click();
        return new BookingDetailsPage1(page);
    }
public void bookingVisibility(String bookingRef) {
    Locator bookingCards = page.locator("#booking-card"); //{local1,loc2,loc3}
    Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
    assertThat(targetBookingCard).isVisible();
    page.locator("#nav-events").click();
}

    public boolean isBookingVisible(String bookingReference) {
        return page.getByText(bookingReference).isVisible();
    }

    public void waitForBookingsToLoad() {
        assertThat(page.getByRole(com.microsoft.playwright.options.AriaRole.HEADING,

            new Page.GetByRoleOptions().setName("My Bookings"))).isVisible();
    }

}
