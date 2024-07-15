package cafemanagement.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.mockito.InjectMocks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.DriverManager;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DatabaseUtilTest {

    @Mock
    private Connection mockConnection;

    private AutoCloseable mocks;

    @Before
    public void setup() throws SQLException {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetConnection_Success() throws SQLException {
        String testConfig = "db.url=jdbc:mysql://localhost:3306/testdb\n" +
                            "db.username=testuser\n" +
                            "db.password=testpass";
        InputStream inputStream = new ByteArrayInputStream(testConfig.getBytes());

        ClassLoader classLoader = getClass().getClassLoader();
        when(classLoader.getResourceAsStream("config.properties")).thenReturn(inputStream);

        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

            Connection connection = DatabaseUtil.getConnection();

            mockedDriverManager.verify(() -> DriverManager.getConnection(eq("jdbc:mysql://localhost:3306/testdb"), eq("testuser"), eq("testpass")));

            assertNotNull(connection);
        }
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_IOError() throws SQLException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        when(classLoader.getResourceAsStream("config.properties")).thenThrow(new IOException());

        DatabaseUtil.getConnection();
    }

    @After
    public void tearDown() throws Exception {
        mocks.close();
    }
}
