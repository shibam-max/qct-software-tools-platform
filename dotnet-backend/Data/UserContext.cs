using Microsoft.EntityFrameworkCore;
using QCT.UserManagement.Models;

namespace QCT.UserManagement.Data;

public class UserContext : DbContext
{
    public UserContext(DbContextOptions<UserContext> options) : base(options)
    {
    }

    public DbSet<User> Users { get; set; }
    public DbSet<Notification> Notifications { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<User>(entity =>
        {
            entity.HasKey(e => e.Id);
            entity.HasIndex(e => e.Email).IsUnique();
            entity.HasIndex(e => e.Username).IsUnique();
            
            entity.Property(e => e.CreatedAt)
                .HasDefaultValueSql("GETUTCDATE()");
                
            entity.Property(e => e.UpdatedAt)
                .HasDefaultValueSql("GETUTCDATE()");
        });
        
        modelBuilder.Entity<Notification>(entity =>
        {
            entity.HasKey(e => e.Id);
            entity.HasIndex(e => e.UserId);
            entity.HasIndex(e => new { e.UserId, e.IsRead });
            
            entity.Property(e => e.CreatedAt)
                .HasDefaultValueSql("GETUTCDATE()");
                
            entity.HasOne(e => e.User)
                .WithMany()
                .HasForeignKey(e => e.UserId)
                .OnDelete(DeleteBehavior.Cascade);
        });
    }
}