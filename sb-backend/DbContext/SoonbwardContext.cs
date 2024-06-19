using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using sb_backend.Models;

namespace sb_backend.DatabaseContext
{
    public partial class SoonbwardContext : DbContext
    {
        public SoonbwardContext()
        {
        }

        public SoonbwardContext(DbContextOptions<SoonbwardContext> options) : base(options)
        {
        }

        public virtual DbSet<Category> Categories { get; set; }
        public virtual DbSet<PrismaMigration> PrismaMigrations { get; set; }
        public virtual DbSet<Sound> Sounds { get; set; }
        public virtual DbSet<User> Users { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseNpgsql("Host=10.117.20.153;Database=soonbward;Username=root;Password=root");
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Category>(entity =>
            {
                entity.HasKey(e => e.Id).HasName("Category_pkey");

                entity.ToTable("Category");

                entity.HasIndex(e => e.UserCategoryId, "Category_userCategoryId_key").IsUnique();

                entity.Property(e => e.Id).HasColumnName("id");
                entity.Property(e => e.UserCategoryId).HasColumnName("userCategoryId");

                entity.HasOne(d => d.UserCategory).WithOne(p => p.Category)
                    .HasForeignKey<Category>(d => d.UserCategoryId)
                    .OnDelete(DeleteBehavior.Restrict)
                    .HasConstraintName("Category_userCategoryId_fkey");
            });

            modelBuilder.Entity<PrismaMigration>(entity =>
            {
                entity.HasKey(e => e.Id).HasName("_prisma_migrations_pkey");

                entity.ToTable("_prisma_migrations");

                entity.Property(e => e.Id)
                    .HasMaxLength(36)
                    .HasColumnName("id");
                entity.Property(e => e.AppliedStepsCount)
                    .HasDefaultValue(0)
                    .HasColumnName("applied_steps_count");
                entity.Property(e => e.Checksum)
                    .HasMaxLength(64)
                    .HasColumnName("checksum");
                entity.Property(e => e.FinishedAt).HasColumnName("finished_at");
                entity.Property(e => e.Logs).HasColumnName("logs");
                entity.Property(e => e.MigrationName)
                    .HasMaxLength(255)
                    .HasColumnName("migration_name");
                entity.Property(e => e.RolledBackAt).HasColumnName("rolled_back_at");
                entity.Property(e => e.StartedAt)
                    .HasDefaultValueSql("now()")
                    .HasColumnName("started_at");
            });

            modelBuilder.Entity<Sound>(entity =>
            {
                entity.HasKey(e => e.Id).HasName("Sound_pkey");

                entity.ToTable("Sound");

                entity.HasIndex(e => e.CategoryId, "Sound_categoryId_key").IsUnique();

                entity.Property(e => e.Id).HasColumnName("id");
                entity.Property(e => e.CategoryId).HasColumnName("categoryId");
                entity.Property(e => e.FileName).HasColumnName("fileName");
                entity.Property(e => e.ImgName).HasColumnName("imgName");
                entity.Property(e => e.IsShared).HasColumnName("isShared");
                entity.Property(e => e.Name).HasColumnName("name");

                entity.HasOne(d => d.Category).WithOne(p => p.Sound)
                    .HasPrincipalKey<Category>(p => p.UserCategoryId)
                    .HasForeignKey<Sound>(d => d.CategoryId)
                    .OnDelete(DeleteBehavior.Restrict)
                    .HasConstraintName("Sound_categoryId_fkey");
            });

            modelBuilder.Entity<User>(entity =>
            {
                entity.HasKey(e => e.Id).HasName("User_pkey");

                entity.ToTable("User");

                entity.HasIndex(e => e.Email, "User_email_key").IsUnique();

                entity.HasIndex(e => e.UserName, "User_userName_key").IsUnique();

                entity.Property(e => e.Id).HasColumnName("id");
                entity.Property(e => e.Email).HasColumnName("email");
                entity.Property(e => e.Password).HasColumnName("password");
                entity.Property(e => e.UserName).HasColumnName("userName");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
