using Microsoft.AspNetCore.Mvc;
using QCT.UserManagement.DTOs;
using QCT.UserManagement.Services;

namespace QCT.UserManagement.Controllers;

[ApiController]
[Route("api/v1/users")]
public class UserController : ControllerBase
{
    private readonly IUserService _userService;
    private readonly ILogger<UserController> _logger;

    public UserController(IUserService userService, ILogger<UserController> logger)
    {
        _userService = userService;
        _logger = logger;
    }

    [HttpPost("register")]
    public async Task<ActionResult<UserResponse>> Register([FromBody] UserRequest request)
    {
        _logger.LogInformation("Registering user: {Email}", request.Email);
        
        var response = await _userService.RegisterUserAsync(request);
        return Ok(response);
    }

    [HttpPost("authenticate")]
    public async Task<ActionResult<AuthResponse>> Authenticate([FromBody] AuthRequest request)
    {
        _logger.LogInformation("Authenticating user: {Email}", request.Email);
        
        var response = await _userService.AuthenticateAsync(request);
        if (response == null)
            return Unauthorized("Invalid credentials");
            
        return Ok(response);
    }

    [HttpGet("{userId}")]
    public async Task<ActionResult<UserResponse>> GetUser(int userId)
    {
        _logger.LogInformation("Fetching user: {UserId}", userId);
        
        var user = await _userService.GetUserAsync(userId);
        if (user == null)
            return NotFound();
            
        return Ok(user);
    }

    [HttpGet]
    public async Task<ActionResult<List<UserResponse>>> GetAllUsers()
    {
        _logger.LogInformation("Fetching all users");
        
        var users = await _userService.GetAllUsersAsync();
        return Ok(users);
    }

    [HttpGet("health")]
    public ActionResult<string> HealthCheck()
    {
        return Ok("User Management Service is healthy");
    }
}