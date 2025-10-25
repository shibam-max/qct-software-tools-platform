using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using Moq;
using QCT.UserManagement.Data;
using QCT.UserManagement.DTOs;
using QCT.UserManagement.Models;
using QCT.UserManagement.Services;
using Xunit;

namespace QCT.UserManagement.Tests;

public class UserServiceTests : IDisposable
{
    private readonly UserContext _context;
    private readonly UserService _userService;
    private readonly Mock<ILogger<UserService>> _mockLogger;

    public UserServiceTests()
    {
        var options = new DbContextOptionsBuilder<UserContext>()
            .UseInMemoryDatabase(databaseName: Guid.NewGuid().ToString())
            .Options;

        _context = new UserContext(options);
        _mockLogger = new Mock<ILogger<UserService>>();
        _userService = new UserService(_context, _mockLogger.Object);
    }

    [Fact]
    public async Task RegisterUserAsync_ShouldCreateUser_WhenValidRequest()
    {
        // Arrange
        var request = new UserRequest
        {
            Username = "testuser",
            Email = "test@example.com",
            Password = "password123",
            FirstName = "Test",
            LastName = "User"
        };

        // Act
        var result = await _userService.RegisterUserAsync(request);

        // Assert
        Assert.NotNull(result);
        Assert.Equal("testuser", result.Username);
        Assert.Equal("test@example.com", result.Email);
        Assert.True(result.IsActive);
    }

    [Fact]
    public async Task AuthenticateAsync_ShouldReturnToken_WhenValidCredentials()
    {
        // Arrange
        var registerRequest = new UserRequest
        {
            Username = "testuser",
            Email = "test@example.com",
            Password = "password123"
        };

        await _userService.RegisterUserAsync(registerRequest);

        var authRequest = new AuthRequest
        {
            Email = "test@example.com",
            Password = "password123"
        };

        // Act
        var result = await _userService.AuthenticateAsync(authRequest);

        // Assert
        Assert.NotNull(result);
        Assert.Equal("test@example.com", result.Email);
        Assert.NotEmpty(result.Token);
    }

    [Fact]
    public async Task GetUserAsync_ShouldReturnUser_WhenUserExists()
    {
        // Arrange
        var user = new User
        {
            Username = "testuser",
            Email = "test@example.com",
            PasswordHash = "hashedpassword"
        };

        _context.Users.Add(user);
        await _context.SaveChangesAsync();

        // Act
        var result = await _userService.GetUserAsync(user.Id);

        // Assert
        Assert.NotNull(result);
        Assert.Equal("testuser", result.Username);
    }

    public void Dispose()
    {
        _context.Dispose();
    }
}