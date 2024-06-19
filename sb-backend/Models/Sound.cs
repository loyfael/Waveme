namespace sb_backend.Models;

public partial class Sound
{
    public int Id { get; set; }

    public int CategoryId { get; set; }

    public string Name { get; set; } = null!;

    public string FileName { get; set; } = null!;

    public string? ImgName { get; set; }

    public bool IsShared { get; set; }

    public virtual Category Category { get; set; } = null!;
}
