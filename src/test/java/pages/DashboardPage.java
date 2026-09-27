package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DashboardPage {

        Page page;
    private static final String get_By_Role ="Explore All Events";


        public DashboardPage(Page page)
        {
            this.page = page;
        }


    public void waitForEventsToLoad()
    {
        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();
    }
    public void clickOnExploreAllEvents(){
    page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName(get_By_Role)).click();
}

}
