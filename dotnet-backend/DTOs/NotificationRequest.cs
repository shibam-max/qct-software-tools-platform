using System.ComponentModel.DataAnnotations;

namespace QCT.UserManagement.DTOs;

public class NotificationRequest
{
    [Required]
    public int UserId { get; set; }
    
    [Required]
    [MaxLength(200)]
    public string Title { get; set; } = string.Empty;
    
    [Required]
    [MaxLength(1000)]
    public string Message { get; set; } = string.Empty;
    
    [Required]
    public NotificationType Type { get; set; }
    
    public NotificationPriority Priority { get; set; } = NotificationPriority.Normal;
    
    public Dictionary<string, object>? Metadata { get; set; }
}

public enum NotificationType
{
    Info,
    Warning,
    Error,
    Success,
    DeviceAlert,
    SystemUpdate
}

public enum NotificationPriority
{
    Low,
    Normal,
    High,
    Critical
}