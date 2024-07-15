// package cafemanagement.client;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.PrintWriter;
// import java.sql.SQLException;
// import java.sql.Timestamp;
// import java.util.ArrayList;
// import java.util.LinkedList;
// import java.util.List;
// import java.util.Queue;

// import cafemanagement.client.EmployeeController;
// import cafemanagement.model.*;
// import cafemanagement.service.*;

// class EmployeeControllerTest {
//     private EmployeeController employeeController;
//     private User currentUser;
//     private Queue<String> notificationsQueue;
//     private PrintWriter writer;
//     private BufferedReader userInput;
//     private NotificationService notificationService;
//     private PollService pollService;
//     private MenuItemService menuItemService;
//     private FeedbackService feedbackService;
//     private UserPreferencesService userPreferencesService;

//     @BeforeEach
//     void setUp() {
//         currentUser = new User(1, "John Doe", "john.doe@example.com", "password");
//         notificationsQueue = new LinkedList<>();
//         writer = Mockito.mock(PrintWriter.class);
//         userInput = Mockito.mock(BufferedReader.class);
//         notificationService = Mockito.mock(NotificationService.class);
//         pollService = Mockito.mock(PollService.class);
//         menuItemService = Mockito.mock(MenuItemService.class);
//         feedbackService = Mockito.mock(FeedbackService.class);
//         userPreferencesService = Mockito.mock(UserPreferencesService.class);

//         employeeController = new EmployeeController(currentUser, notificationsQueue, writer, userInput);
//         employeeController.notificationService = notificationService;
//         employeeController.pollService = pollService;
//         employeeController.menuItemService = menuItemService;
//         employeeController.feedbackService = feedbackService;
//         employeeController.userPreferencesService = userPreferencesService;
//     }

//     @Test
//     void testShowNotifications_NoNotifications() throws IOException {
//         when(notificationService.getUnreadNotifications(currentUser.getUserId())).thenReturn(new ArrayList<>());

//         employeeController.showNotifications();

//         verify(notificationService, times(1)).getUnreadNotifications(currentUser.getUserId());
//         assertTrue(notificationsQueue.isEmpty());
//     }

//     @Test
//     void testShowNotifications_WithNotifications() throws IOException {
//         List<Notification> notifications = new ArrayList<>();
//         Timestamp currentTime = new Timestamp(System.currentTimeMillis());
//         notifications.add(new Notification(1, currentUser.getUserId(), "Type", 1, "Test message", currentTime));

//         when(notificationService.getUnreadNotifications(currentUser.getUserId())).thenReturn(notifications);

//         employeeController.showNotifications();

//         verify(notificationService, times(1)).markNotificationAsRead(1, currentUser.getUserId());
//     }

//     @Test
//     void testProvideFeedback_ValidCategoryAndMenu() throws IOException, SQLException {
//         when(userInput.readLine()).thenReturn("1").thenReturn("1").thenReturn("1").thenReturn("1").thenReturn("1").thenReturn("1").thenReturn("Good");
//         List<Menu> menuItems = new ArrayList<>();
//         menuItems.add(new Menu(1, "Pasta", 2, 100, true, "Medium", "Italian", false, "Vegetarian"));
//         when(menuItemService.getMenuItemsDetailsByCategory(1)).thenReturn(menuItems);
//         when(feedbackService.getFeedbackByEmployeeAndMenu(currentUser.getUserId(), 1)).thenReturn(null);

//         employeeController.provideFeedback();

//         verify(feedbackService, times(1)).storeFeedback(any(Feedback.class));
//     }

//     @Test
//     void testProvideFeedback_ExistingFeedback() throws IOException, SQLException {
//         when(userInput.readLine()).thenReturn("1").thenReturn("1").thenReturn("yes").thenReturn("5").thenReturn("5").thenReturn("5").thenReturn("5").thenReturn("5").thenReturn("Updated feedback");
//         List<Menu> menuItems = new ArrayList<>();
//         menuItems.add(new Menu(1, "Pasta", 2, 100, true, "Medium", "Italian", false, "Vegetarian"));
//         when(menuItemService.getMenuItemsDetailsByCategory(1)).thenReturn(menuItems);
//         Feedback existingFeedback = new Feedback(1, 1, 1, 3, 3, 3, 3, 3, "Old feedback", new java.sql.Date(System.currentTimeMillis()));
//         when(feedbackService.getFeedbackByEmployeeAndMenu(currentUser.getUserId(), 1)).thenReturn(existingFeedback);

//         employeeController.provideFeedback();

//         verify(feedbackService, times(1)).updateFeedback(any(Feedback.class));
//     }

//     @Test
//     void testSelectMealForTomorrow_NoPollItems() {
//         when(pollService.getPollItemsForToday()).thenReturn(new ArrayList<>());

//         employeeController.selectMealForTomorrow();

//         verify(pollService, times(1)).getPollItemsForToday();
//     }

//     @Test
//     void testSelectMealForTomorrow_HasVoted() {
//         List<PollItem> pollItems = new ArrayList<>();
//         pollItems.add(new PollItem(1, 1, 1, "Pasta"));
//         when(pollService.getPollItemsForToday()).thenReturn(pollItems);
//         when(pollService.hasVotedToday(1, currentUser.getUserId())).thenReturn(true);

//         employeeController.selectMealForTomorrow();

//         verify(pollService, times(1)).hasVotedToday(1, currentUser.getUserId());
//     }

//     @Test
//     void testSelectMealForTomorrow_ValidSelection() throws IOException {
//         List<PollItem> pollItems = new ArrayList<>();
//         pollItems.add(new PollItem(1, 1, 1,"Pasta"));
//         when(pollService.getPollItemsForToday()).thenReturn(pollItems);
//         when(pollService.hasVotedToday(1, currentUser.getUserId())).thenReturn(false);
//         when(userInput.readLine()).thenReturn("1");

//         employeeController.selectMealForTomorrow();

//         verify(pollService, times(1)).castVote(1, 1, currentUser.getUserId());
//     }

//     @Test
//     void testViewMenu_NoMenuItems() {
//         when(menuItemService.getAllMenuItems()).thenReturn(new ArrayList<>());

//         employeeController.viewMenu();

//         verify(menuItemService, times(1)).getAllMenuItems();
//     }

//     @Test
//     void testViewMenu_WithMenuItems() {
//         List<Menu> menuItems = new ArrayList<>();
//         menuItems.add(new Menu(1, "Pasta", 2, 100, true, "Medium", "Italian", false, "Vegetarian"));
//         when(menuItemService.getAllMenuItems()).thenReturn(menuItems);

//         employeeController.viewMenu();

//         verify(menuItemService, times(1)).getAllMenuItems();
//     }

//     @Test
//     void testUpdateProfile_NewProfile() throws IOException, SQLException {
//         when(userInput.readLine()).thenReturn("1").thenReturn("1").thenReturn("1").thenReturn("1").thenReturn("yes");
//         when(userPreferencesService.getPreferencesByEmployeeId(currentUser.getUserId())).thenReturn(null);

//         employeeController.updateProfile();

//         verify(userPreferencesService, times(1)).insertUserPreferences(any(UserPreferences.class));
//     }

//     @Test
//     void testUpdateProfile_ExistingProfile() throws IOException {
//         UserPreferences preferences = new UserPreferences(currentUser.getUserId(), "Vegetarian", "Medium", "Italian", true);
//         when(userInput.readLine()).thenReturn("4").thenReturn("4").thenReturn("7").thenReturn("3");
//         when(userPreferencesService.getPreferencesByEmployeeId(currentUser.getUserId())).thenReturn(preferences);

//         employeeController.updateProfile();

//         verify(userPreferencesService, times(1)).updateUserPreferences(any(UserPreferences.class));
//     }

//     @Test
//     void testLogout() {
//         employeeController.logout();

//         verify(writer, times(1)).println("LOGOUT");
//     }

//     @Test
//     void testHandleInput_ValidInput() throws IOException {
//         doNothing().when(employeeController).showNotifications();

//         employeeController.handleInput("1");

//         verify(employeeController, times(1)).showNotifications();
//     }

//     @Test
//     void testHandleInput_InvalidInput() {
//         employeeController.handleInput("invalid");

//         assertTrue(notificationsQueue.isEmpty());
//     }

//     @Test
//     void testDisplayMenu() {
//         employeeController.displayMenu();
//     }

//     @Test
//     void testSortPollItems() {
//         List<PollItem> pollItems = new ArrayList<>();
//         PollItem item1 = new PollItem(1, 1, 1, "Item 1");
//         PollItem item2 = new PollItem(2, 2, 2, "Item 2");
//         pollItems.add(item1);
//         pollItems.add(item2);

//         UserPreferences preferences = new UserPreferences(currentUser.getUserId(), "Vegetarian", "Medium", "Indian", true);
//         when(menuItemService.getMenuItemById(1)).thenReturn(new Menu(1, "Item 1", 1, 100, true, "Medium", "Indian", false, "Vegetarian"));
//         when(menuItemService.getMenuItemById(2)).thenReturn(new Menu(2, "Item 2", 1, 100, true, "Medium", "Italian", false, "Vegetarian"));

//         List<PollItem> sortedPollItems = employeeController.sortPollItems(pollItems, preferences);

//         assertEquals(2, sortedPollItems.size());
//         assertEquals(item1, sortedPollItems.get(0));
//         assertEquals(item2, sortedPollItems.get(1));
//     }
// }
