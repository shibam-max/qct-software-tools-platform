using Microsoft.AspNetCore.Mvc;
using QCT.UserManagement.DTOs;
using QCT.UserManagement.Services;

namespace QCT.UserManagement.Controllers;

[ApiController]
[Route("api/v1/notifications")]
public class NotificationController : ControllerBase
{
    private readonly INotificationService _notificationService;
    private readonly ILogger<NotificationController> _logger;

    public NotificationController(INotificationService notificationService, ILogger<NotificationController> logger)
    {
        _notificationService = notificationService;
        _logger = logger;
    }

    [HttpPost("send")]
    public async Task<ActionResult<NotificationResponse>> SendNotification([FromBody] NotificationRequest request)
    {
        _logger.LogInformation("Sending notification to user: {UserId}", request.UserId);
        
        var response = await _notificationService.SendNotificationAsync(request);
        return Ok(response);
    }

    [HttpGet("user/{userId}")]
    public async Task<ActionResult<List<NotificationResponse>>> GetUserNotifications(int userId)
    {
        _logger.LogInformation("Fetching notifications for user: {UserId}", userId);
        
        var notifications = await _notificationService.GetUserNotificationsAsync(userId);
        return Ok(notifications);
    }

    [HttpPut("{notificationId}/read")]
    public async Task<ActionResult<NotificationResponse>> MarkAsRead(int notificationId)
    {
        _logger.LogInformation("Marking notification as read: {NotificationId}", notificationId);
        
        var response = await _notificationService.MarkAsReadAsync(notificationId);
        if (response == null)
            return NotFound();
            
        return Ok(response);
    }

    [HttpGet("unread/count/{userId}")]
    public async Task<ActionResult<int>> GetUnreadCount(int userId)
    {
        _logger.LogInformation("Getting unread count for user: {UserId}", userId);
        
        var count = await _notificationService.GetUnreadCountAsync(userId);
        return Ok(count);
    }

    [HttpGet("health")]
    public ActionResult<string> HealthCheck()
    {
        return Ok("Notification Service is healthy");
    }
}