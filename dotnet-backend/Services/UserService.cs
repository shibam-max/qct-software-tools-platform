using Microsoft.EntityFrameworkCore;
using QCT.UserManagement.Data;
using QCT.UserManagement.DTOs;
using QCT.UserManagement.Models;

namespace QCT.UserManagement.Services;

public interface IUserService
{
    Task<UserResponse> RegisterUserAsync(UserRequest request);
    Task<AuthResponse?> AuthenticateAsync(AuthRequest request);
    Task<UserResponse?> GetUserAsync(int userId);
    Task<List<UserResponse>> GetAllUsersAsync();
}

public class UserService : IUserService
{
    private readonly UserContext _context;
    private readonly ILogger<UserService> _logger;

    public UserService(UserContext context, ILogger<UserService> logger)
    {
        _context = context;
        _logger = logger;
    }

    public async Task<UserResponse> RegisterUserAsync(UserRequest request)
    {
        _logger.LogInformation("Registering new user: {Email}", request.Email);

        var user = new User
        {
            Username = request.Username,
            Email = request.Email,
            PasswordHash = BCrypt.Net.BCrypt.HashPassword(request.Password),
            FirstName = request.FirstName,
            LastName = request.LastName
        };

        _context.Users.Add(user);
        await _context.SaveChangesAsync();

        return MapToResponse(user);
    }

    public async Task<AuthResponse?> AuthenticateAsync(AuthRequest request)
    {
        _logger.LogInformation("Authenticating user: {Email}", request.Email);

        var user = await _context.Users
            .FirstOrDefaultAsync(u => u.Email == request.Email && u.IsActive);

        if (user == null || !BCrypt.Net.BCrypt.Verify(request.Password, user.PasswordHash))
        {
            return null;
        }

        return new AuthResponse
        {
            UserId = user.Id,
            Username = user.Username,
            Email = user.Email,
            Token = GenerateToken(user),
            ExpiresAt = DateTime.UtcNow.AddHours(24)
        };
    }

    public async Task<UserResponse?> GetUserAsync(int userId)
    {
        var user = await _context.Users
            .FirstOrDefaultAsync(u => u.Id == userId && u.IsActive);

        return user != null ? MapToResponse(user) : null;
    }

    public async Task<List<UserResponse>> GetAllUsersAsync()
    {
        var users = await _context.Users
            .Where(u => u.IsActive)
            .ToListAsync();

        return users.Select(MapToResponse).ToList();
    }

    private static UserResponse MapToResponse(User user)
    {
        return new UserResponse
        {
            Id = user.Id,
            Username = user.Username,
            Email = user.Email,
            FirstName = user.FirstName,
            LastName = user.LastName,
            IsActive = user.IsActive,
            CreatedAt = user.CreatedAt,
            UpdatedAt = user.UpdatedAt
        };
    }

    private static string GenerateToken(User user)
    {
        // Simple token generation for demo
        return Convert.ToBase64String(System.Text.Encoding.UTF8.GetBytes($"{user.Id}:{user.Email}:{DateTime.UtcNow:yyyy-MM-dd}"));
    }
}