using Microsoft.EntityFrameworkCore;
using QCT.UserManagement.Data;
using QCT.UserManagement.DTOs;
using QCT.UserManagement.Models;
using System.Text.Json;

namespace QCT.UserManagement.Services;

public interface INotificationService
{
    Task<NotificationResponse> SendNotificationAsync(NotificationRequest request);
    Task<List<NotificationResponse>> GetUserNotificationsAsync(int userId);
    Task<NotificationResponse?> MarkAsReadAsync(int notificationId);
    Task<int> GetUnreadCountAsync(int userId);
}

public class NotificationService : INotificationService
{
    private readonly UserContext _context;
    private readonly ILogger<NotificationService> _logger;

    public NotificationService(UserContext context, ILogger<NotificationService> logger)
    {
        _context = context;
        _logger = logger;
    }

    public async Task<NotificationResponse> SendNotificationAsync(NotificationRequest request)
    {
        _logger.LogInformation("Creating notification for user: {UserId}", request.UserId);

        var notification = new Notification
        {
            UserId = request.UserId,
            Title = request.Title,
            Message = request.Message,
            Type = request.Type,
            Priority = request.Priority,
            MetadataJson = request.Metadata != null ? JsonSerializer.Serialize(request.Metadata) : null
        };

        _context.Notifications.Add(notification);
        await _context.SaveChangesAsync();

        _logger.LogInformation("Notification created with ID: {NotificationId}", notification.Id);

        return MapToResponse(notification);
    }

    public async Task<List<NotificationResponse>> GetUserNotificationsAsync(int userId)
    {
        _logger.LogInformation("Fetching notifications for user: {UserId}", userId);

        var notifications = await _context.Notifications
            .Where(n => n.UserId == userId)
            .OrderByDescending(n => n.CreatedAt)
            .Take(50) // Limit to recent 50 notifications
            .ToListAsync();

        return notifications.Select(MapToResponse).ToList();
    }

    public async Task<NotificationResponse?> MarkAsReadAsync(int notificationId)
    {
        _logger.LogInformation("Marking notification as read: {NotificationId}", notificationId);

        var notification = await _context.Notifications
            .FirstOrDefaultAsync(n => n.Id == notificationId);

        if (notification == null)
        {
            _logger.LogWarning("Notification not found: {NotificationId}", notificationId);
            return null;
        }

        notification.IsRead = true;
        notification.ReadAt = DateTime.UtcNow;

        await _context.SaveChangesAsync();

        return MapToResponse(notification);
    }

    public async Task<int> GetUnreadCountAsync(int userId)
    {
        _logger.LogInformation("Getting unread count for user: {UserId}", userId);

        return await _context.Notifications
            .CountAsync(n => n.UserId == userId && !n.IsRead);
    }

    private static NotificationResponse MapToResponse(Notification notification)
    {
        Dictionary<string, object>? metadata = null;
        if (!string.IsNullOrEmpty(notification.MetadataJson))
        {
            try
            {
                metadata = JsonSerializer.Deserialize<Dictionary<string, object>>(notification.MetadataJson);
            }
            catch (JsonException)
            {
                // Log error and continue without metadata
            }
        }

        return new NotificationResponse
        {
            Id = notification.Id,
            UserId = notification.UserId,
            Title = notification.Title,
            Message = notification.Message,
            Type = notification.Type,
            Priority = notification.Priority,
            IsRead = notification.IsRead,
            CreatedAt = notification.CreatedAt,
            ReadAt = notification.ReadAt,
            Metadata = metadata
        };
    }
}