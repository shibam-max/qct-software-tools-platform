using System.ComponentModel.DataAnnotations;
using QCT.UserManagement.DTOs;

namespace QCT.UserManagement.Models;

public class Notification
{
    public int Id { get; set; }
    
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
    
    public bool IsRead { get; set; } = false;
    
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    
    public DateTime? ReadAt { get; set; }
    
    public string? MetadataJson { get; set; }
    
    // Navigation property
    public User? User { get; set; }
}