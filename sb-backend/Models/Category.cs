namespace sb_backend.Models;

public partial class Category
{
    public int Id { get; set; }

    public int UserCategoryId { get; set; }

    public virtual Sound? Sound { get; set; }

    public virtual User UserCategory { get; set; } = null!;
}
