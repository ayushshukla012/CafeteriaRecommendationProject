package cafemanagement.server;

// import cafemanagement.client.AdminControllerTest;
import cafemanagement.client.ChefController;
import cafemanagement.client.EmployeeController;
import cafemanagement.model.User;
import cafemanagement.service.UserService;
import cafemanagement.service.PollService;
import cafemanagement.server.ClientHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClientHandlerTest {

    private Socket mockSocket;
    private UserService mockUserService;
    private PollService mockPollService;
    private ConcurrentHashMap<String, ClientHandler> mockClients;
    private PrintWriter mockWriter;
    private BufferedReader mockReader;
    private ClientHandler clientHandler;

    @BeforeEach
    void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        mockSocket = mock(Socket.class);
        mockUserService = mock(UserService.class);
        mockPollService = mock(PollService.class);
        mockClients = new ConcurrentHashMap<>();

        OutputStream mockOutputStream = new ByteArrayOutputStream();
        InputStream mockInputStream = new ByteArrayInputStream("".getBytes());

        when(mockSocket.getInputStream()).thenReturn(mockInputStream);
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

        clientHandler = new ClientHandler(mockSocket, mockClients);

        Field userServiceField = ClientHandler.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(clientHandler, mockUserService);

        Field pollServiceField = ClientHandler.class.getDeclaredField("pollService");
        pollServiceField.setAccessible(true);
        pollServiceField.set(clientHandler, mockPollService);

        mockWriter = new PrintWriter(mockOutputStream, true);
        mockReader = new BufferedReader(new InputStreamReader(mockInputStream));
    }
    

    // @Test
    // void testHandleEmployeeCommand() throws IOException, InterruptedException {
    //     User mockUser = new User(1, "John Doe", "password", "Employee");
    //     when(mockUserService.authenticate(1, "password", "Employee")).thenReturn(mockUser);

    //     ByteArrayInputStream employeeCommandStream = new ByteArrayInputStream("LOGIN:1:password:Employee\nEMPLOYEE_COMMAND\n".getBytes());
    //     when(mockSocket.getInputStream()).thenReturn(employeeCommandStream);

    //     Thread clientThread = new Thread(clientHandler);
    //     clientThread.start();
    //     clientThread.join(); // Wait for client handler thread to finish execution

    //     // Verify employee-specific command handling
    //     EmployeeController mockEmployeeController = mock(EmployeeController.class);
    //     verify(mockEmployeeController, times(1)).handleInput("EMPLOYEE_COMMAND");
    // }

    // @Test
    // void testHandleChefCommand() throws IOException, InterruptedException {
    //     User mockUser = new User(2, "Jane Doe", "password", "Chef");
    //     when(mockUserService.authenticate(2, "password", "Chef")).thenReturn(mockUser);

    //     ByteArrayInputStream chefCommandStream = new ByteArrayInputStream("LOGIN:2:password:Chef\nCHEF_COMMAND\n".getBytes());
    //     when(mockSocket.getInputStream()).thenReturn(chefCommandStream);

    //     Thread clientThread = new Thread(clientHandler);
    //     clientThread.start();
    //     clientThread.join(); // Wait for client handler thread to finish execution

    //     // Verify chef-specific command handling
    //     ChefController mockChefController = mock(ChefController.class);
    //     verify(mockChefController, times(1)).handleInput("CHEF_COMMAND");
    // }

    // @Test
    // void testHandleAdminCommand() throws IOException, InterruptedException {
    //     User mockUser = new User(3, "Admin User", "password", "Admin");
    //     when(mockUserService.authenticate(3, "password", "Admin")).thenReturn(mockUser);

    //     ByteArrayInputStream adminCommandStream = new ByteArrayInputStream("LOGIN:3:password:Admin\nADMIN_COMMAND\n".getBytes());
    //     when(mockSocket.getInputStream()).thenReturn(adminCommandStream);

    //     Thread clientThread = new Thread(clientHandler);
    //     clientThread.start();
    //     clientThread.join(); // Wait for client handler thread to finish execution

    //     // Verify admin-specific command handling
    //     AdminController mockAdminController = mock(AdminController.class);
    //     verify(mockAdminController, times(1)).handleInput("ADMIN_COMMAND");
    // }

    // @Test
    // void testHandleInvalidRoleCommand() throws IOException, InterruptedException {
    //     User mockUser = new User(4, "Invalid User", "password", "InvalidRole");
    //     when(mockUserService.authenticate(4, "password", "InvalidRole")).thenReturn(mockUser);

    //     ByteArrayInputStream invalidRoleCommandStream = new ByteArrayInputStream("LOGIN:4:password:InvalidRole\nCOMMAND\n".getBytes());
    //     when(mockSocket.getInputStream()).thenReturn(invalidRoleCommandStream);

    //     Thread clientThread = new Thread(clientHandler);
    //     clientThread.start();
    //     clientThread.join(); // Wait for client handler thread to finish execution

    //     // Verify handling for invalid role
    //     verify(mockWriter, times(1)).println("ERROR: Invalid role.");
    // }
}
