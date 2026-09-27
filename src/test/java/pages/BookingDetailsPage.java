package pages;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingDetailsPage {

    Page page;
    private static final String CANCEL_BOOKING_BUTTON = "button:has-text(\"Cancel Booking\")";
    private static final String YES_CANCEL_BUTTON = "button:has-text(\"Yes, cancel it\")";

    public BookingDetailsPage(Page page) {
        this.page = page;
    }

    public void verifyBookingDetailsPageIsDisplayed() {
        assertThat(page.getByRole(com.microsoft.playwright.options.AriaRole.HEADING, 
            new Page.GetByRoleOptions().setName("Event Details"))).isVisible();
    }

    public String getBookingReference() {
        return page.locator("generic:has-text('P-') >> nth=0").textContent().trim();
    }

    public String getEventTitle() {
        return page.getByRole(com.microsoft.playwright.options.AriaRole.HEADING, 
            new Page.GetByRoleOptions().setLevel(1))
            .nth(0)
            .textContent()
            .trim();
    }

    public void cancelBooking() {
        page.locator(CANCEL_BOOKING_BUTTON).click();
        // Handle confirmation dialog
        page.locator(YES_CANCEL_BUTTON).click();
    }

    public void verifyBookingCancellationSuccess() {
        assertThat(page).hasURL("https://eventhub.rahulshettyacademy.com/bookings");
        assertThat(page.getByText("Booking cancelled successfully")).isVisible();
    }

}
