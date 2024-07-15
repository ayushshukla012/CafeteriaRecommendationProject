// package cafemanagement.client;

// import cafemanagement.model.Menu;
// import cafemanagement.model.User;
// import cafemanagement.service.MenuItemService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.*;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.PrintWriter;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;

// public class AdminControllerTest {

//     @Mock
//     private User mockUser;

//     @Mock
//     private PrintWriter mockWriter;

//     @Mock
//     private BufferedReader mockUserInput;

//     @Mock
//     private MenuItemService mockMenuItemService;

//     @InjectMocks
//     private AdminController adminController;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//         adminController = new AdminController(mockUser, mockWriter, mockUserInput);
//         adminController.menuItemService = mockMenuItemService;
//     }

//     @Test
//     void testHandleInput_AddMenuItem() throws IOException {
//         doNothing().when(mockUserInput).readLine();
//         AdminController spyController = spy(adminController);
//         doNothing().when(spyController).addMenuItem();
//         spyController.handleInput("1");
//         verify(spyController).addMenuItem();
//     }

//     @Test
//     void testHandleInput_UpdateMenuItem() throws IOException {
//         doNothing().when(mockUserInput).readLine();
//         AdminController spyController = spy(adminController);
//         doNothing().when(spyController).updateMenuItem();
//         spyController.handleInput("2");
//         verify(spyController).updateMenuItem();
//     }

//     @Test
//     void testHandleInput_DeleteMenuItem() throws IOException {
//         doNothing().when(mockUserInput).readLine();
//         AdminController spyController = spy(adminController);
//         doNothing().when(spyController).deleteMenuItem();
//         spyController.handleInput("3");
//         verify(spyController).deleteMenuItem();
//     }

//     @Test
//     void testHandleInput_CheckItemAvailability() throws IOException {
//         doNothing().when(mockUserInput).readLine();
//         AdminController spyController = spy(adminController);
//         doNothing().when(spyController).checkItemAvailability();
//         spyController.handleInput("4");
//         verify(spyController).checkItemAvailability();
//     }

//     @Test
//     void testHandleInput_ViewFullMenu() throws IOException {
//         doNothing().when(mockUserInput).readLine();
//         AdminController spyController = spy(adminController);
//         doNothing().when(spyController).viewFullMenu();
//         spyController.handleInput("5");
//         verify(spyController).viewFullMenu();
//     }

//     @Test
//     void testHandleInput_Logout() throws IOException {
//         doNothing().when(mockUserInput).readLine();
//         adminController.handleInput("6");
//         verify(mockWriter).println("LOGOUT");
//     }

//     @Test
//     void testHandleInput_InvalidInput() throws IOException {
//         // Simulate IOException when readLine() is called with "invalid"
//         when(mockUserInput.readLine())
//                 .thenReturn("invalid")  // Provide the input that triggers exception
//                 .thenThrow(new IOException());  // Simulate IOException
    
//         // Call the method under test
//         adminController.handleInput("1");  // Assuming "1" triggers the invalid input path
    
//         // Verify that println was called with the expected message
//         verify(mockWriter).println("Invalid input. Please enter a number.");
//     }

//     @Test
//     void testAddMenuItem_Success() throws IOException {
//         when(mockUserInput.readLine())
//                 .thenReturn("1")
//                 .thenReturn("Test Item")
//                 .thenReturn("10.5")
//                 .thenReturn("yes");
//         when(mockMenuItemService.storeMenuItem(anyString(), anyInt(), anyFloat(), anyBoolean()))
//                 .thenReturn(true);
//         adminController.addMenuItem();
//         verify(mockWriter).println("ADD_MENU_ITEM:1:Test Item:10.5:true");
//         verify(mockWriter).println("Menu item added successfully.");
//     }

//     @Test
//     void testAddMenuItem_InvalidCategory() throws IOException {
//         when(mockUserInput.readLine()).thenReturn("4");
//         adminController.addMenuItem();
//         verify(mockWriter).println("Invalid category selected.");
//     }

//     @Test
//     void testUpdateMenuItem_Success() throws IOException {
//         List<String> menuItems = Arrays.asList("Item 1", "Item 2");
//         when(mockUserInput.readLine())
//                 .thenReturn("1")
//                 .thenReturn("1")
//                 .thenReturn("2")
//                 .thenReturn("15.5");
//         when(mockMenuItemService.getMenuItemsByCategory(anyInt())).thenReturn(menuItems);
//         when(mockMenuItemService.updateMenuInDatabase(anyString(), anyString(), anyFloat())).thenReturn(true);
//         adminController.updateMenuItem();
//         verify(mockWriter).println("Menu item updated successfully.");
//     }

//     @Test
//     void testUpdateMenuItem_InvalidCategory() throws IOException {
//         when(mockUserInput.readLine()).thenReturn("4");
//         adminController.updateMenuItem();
//         verify(mockWriter).println("Invalid category selected.");
//     }

//     @Test
//     void testDeleteSingleMenuItem_Success() throws IOException {
//         List<Menu> menuItems = new ArrayList<>();
//         Menu menu = new Menu();
//         menu.setMenuId(1);
//         menu.setName("Test Item");
//         menuItems.add(menu);
//         when(mockUserInput.readLine())
//                 .thenReturn("1")
//                 .thenReturn("1")
//                 .thenReturn("yes");
//         when(mockMenuItemService.getMenuItemsDetailsByCategory(anyInt())).thenReturn(menuItems);
//         when(mockMenuItemService.deleteMenuItem(anyInt())).thenReturn(true);
//         adminController.deleteSingleMenuItem(1);
//         verify(mockWriter).println("Menu item deleted successfully.");
//     }

//     @Test
//     void testDeleteSingleMenuItem_InvalidCategory() throws IOException {
//         when(mockUserInput.readLine()).thenReturn("4");
//         adminController.deleteMenuItem();
//         verify(mockWriter).println("Invalid category selected.");
//     }

//     @Test
//     void testCheckItemAvailability() {
//         adminController.checkItemAvailability();
//         verify(mockWriter).println("Checking item availability...");
//     }

//     @Test
//     void testViewFullMenu() {
//         List<Menu> menuItems = Arrays.asList(new Menu());
//         when(mockMenuItemService.getAllMenuItems()).thenReturn(menuItems);
//         adminController.viewFullMenu();
//         verify(mockWriter).println("Viewing Menu With Feedback...");
//     }

//     @Test
//     void testLogout() {
//         adminController.logout();
//         verify(mockWriter).println("LOGOUT");
//     }
// }
