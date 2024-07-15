// package cafemanagement.client;

// import cafemanagement.model.Feedback;
// import cafemanagement.model.Menu;
// import cafemanagement.model.User;
// import cafemanagement.service.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.*;

// import java.io.BufferedReader;
// import java.io.PrintWriter;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;
// import java.text.SimpleDateFormat;
// import java.util.Date;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyDouble;
// import static org.mockito.ArgumentMatchers.anyInt;
// import static org.mockito.ArgumentMatchers.anyList;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.*;

// public class ChefControllerTest {

//     @Mock private User mockCurrentUser;
//     @Mock private PrintWriter mockWriter;
//     @Mock private BufferedReader mockUserInput;
//     @Mock private NotificationService mockNotificationService;
//     @Mock private UserService mockUserService;
//     @Mock private PollService mockPollService;
//     @Mock private MenuItemService mockMenuItemService;
//     @Mock private RecommendationService mockRecommendationService;
//     @Mock private FeedbackService mockFeedbackService;
    
//     @InjectMocks private ChefController chefController;

//     @BeforeEach
//     public void setUp() {
//         MockitoAnnotations.openMocks(this);
//         chefController = new ChefController(mockCurrentUser, mockWriter, mockUserInput);
//         chefController.notificationService = mockNotificationService;
//         chefController.userService = mockUserService;
//         chefController.pollService = mockPollService;
//         chefController.menuItemService = mockMenuItemService;
//         chefController.recommendationService = mockRecommendationService;
//         chefController.feedbackService = mockFeedbackService;
//     }

//     @Test
//     public void testDisplayMenu() {
//         List<Menu> menuItems = new ArrayList<>();
//         menuItems.add(new Menu(1, "Pancakes", 1, 5, true, "Mild", "American", false, "Vegetarian"));
//         menuItems.add(new Menu(2, "Spaghetti", 2, 8, true, "Medium", "Italian", false, "Non-Vegetarian"));
//         menuItems.add(new Menu(3, "Ice Cream", 3, 3, true, "None", "Dessert", true, "Vegetarian"));

//         when(mockMenuItemService.getAllMenuItems()).thenReturn(menuItems);

//         chefController.viewMenu();

//         verify(mockWriter).println("Viewing Menu With Feedback...");
//         verify(mockWriter).println("Menu items:");
//         verify(mockWriter, times(4)).println(anyString());
//         verify(mockWriter).printf(anyString(), anyInt(), anyString(), anyInt(), anyDouble(), anyString(), anyString(), anyString(), anyString(), anyString());
//     }

//     // @Test
//     // public void testSendEmployeeNotification() throws Exception {
//     //     when(mockUserInput.readLine()).thenReturn("1", "This is a test notification message.");
//     //     when(mockUserService.getAllEmployeeIds()).thenReturn(List.of(1, 2, 3));
//     //     when(mockCurrentUser.getUserId()).thenReturn(1);

//     //     chefController.sendEmployeeNotification();

//     //     verify(mockNotificationService).sendNotification(eq(1), eq("Recommendation"), eq(1), eq("This is a test notification message."), anyList());
//     //     verify(mockWriter).println("Notification for Recommendation sent.");
//     // }

//     @Test
//     public void testGenerateFeedbackReport() throws Exception {
//         List<Feedback> feedbackList = new ArrayList<>();
//         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//         Date feedbackDate = sdf.parse("2022-07-15");
//         feedbackList.add(new Feedback(1, 1, 5, 4, 3, 2, 5, 3, "Good", feedbackDate));
//         feedbackList.add(new Feedback(2, 1, 3, 3, 3, 3, 3, 4, "Average", feedbackDate));
        
//         List<Menu> menuItems = new ArrayList<>();
//         menuItems.add(new Menu(1, "Pancakes", 1, 5, true, "Mild", "American", false, "Vegetarian"));

//         when(mockFeedbackService.getAllFeedback()).thenReturn(feedbackList);
//         when(mockMenuItemService.getAllMenuItems()).thenReturn(menuItems);

//         chefController.generateFeedbackReport();

//         verify(mockWriter).println("Generating feedback report...");
//         verify(mockWriter).println("Feedback report generated successfully at: " + anyString());
//     }

//     // @Test
//     // public void testSendDishesToReview() throws Exception {
//     //     when(mockUserInput.readLine()).thenReturn("2", "1,2,3");
//     //     List<Map<String, Object>> recommendedItems = new ArrayList<>();
//     //     recommendedItems.add(Map.of("name", "Pasta", "menuId", 1, "categoryId", 2, "price", 10.0, "availability", true, "averageRating", 4.5, "sentiment", "Positive"));
//     //     recommendedItems.add(Map.of("name", "Pizza", "menuId", 2, "categoryId", 2, "price", 8.0, "availability", true, "averageRating", 4.0, "sentiment", "Positive"));

//     //     when(mockRecommendationService.recommendFood(2)).thenReturn(recommendedItems);
//     //     when(mockPollService.createPoll(anyInt(), any())).thenReturn(1);

//     //     chefController.sendDishesToReview();

//     //     verify(mockPollService).createPoll(anyInt(), any());
//     //     verify(mockPollService).addItemsToPoll(anyInt(), anyList());
//     //     verify(mockWriter).println("Poll created successfully with ID: 1");
//     // }

//     @Test
//     public void testGetDiscardedMenu() throws Exception {
//         List<Feedback> feedbackList = new ArrayList<>();
//         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//         Date feedbackDate = sdf.parse("2024-07-15");
//         feedbackList.add(new Feedback(1, 1, 1, 1, 1, 1, 1, 1, "Bad", feedbackDate));
//         feedbackList.add(new Feedback(2, 1, 1, 1, 1, 1, 1, 1, "Very Bad", feedbackDate));

//         List<Menu> menuItems = new ArrayList<>();
//         menuItems.add(new Menu(1, "Pancakes", 1, 5, true, "Mild", "American", false, "Vegetarian"));

//         when(mockFeedbackService.getAllFeedback()).thenReturn(feedbackList);
//         when(mockMenuItemService.getAllMenuItems()).thenReturn(menuItems);
//         when(mockRecommendationService.calculateSentiment(anyList())).thenReturn("very negative");

//         chefController.getDiscardedMenu();

//         verify(mockWriter).println("Fetching discarded menu items...");
//         verify(mockWriter, times(3)).println(anyString());
//         verify(mockWriter).printf(anyString(), anyInt(), anyString(), anyDouble(), anyString(), anyDouble(), anyString());
//     }

//     @Test
//     public void testLogout() {
//         chefController.logout();

//         verify(mockWriter).println("LOGOUT");
//         verify(mockWriter).println("Logged out.");
//     }

//     @Test
//     public void testHandleInput_Invalid() throws Exception {
//         when(mockUserInput.readLine()).thenReturn("invalid");

//         chefController.handleInput("invalid");

//         verify(mockWriter).println("Invalid input. Please enter a number.");
//     }

//     @Test
//     public void testHandleInput_Valid() throws Exception {
//         when(mockUserInput.readLine()).thenReturn("1");

//         chefController.handleInput("1");

//         verify(mockWriter, never()).println("Invalid input. Please enter a number.");
//     }

//     @Test
//     public void testGetRecommendation() throws Exception {
//         when(mockUserInput.readLine()).thenReturn("The food was excellent!");
//         when(mockRecommendationService.calculateUserFeedbackSentiment(anyString())).thenReturn("Positive");

//         chefController.getRecommendation();

//         verify(mockWriter).println("Calculating recommendation for you. Please wait!!!");
//         verify(mockWriter).println("Sentiment: Positive");
//     }
// }
