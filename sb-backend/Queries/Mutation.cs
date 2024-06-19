using System.Threading.Tasks;
using sb_backend.DatabaseContext;
using sb_backend.Models;

public class Mutation
{
    private readonly SoonbwardContext _context;

    public Mutation(SoonbwardContext context)
    {
        _context = context;
    }

    public async Task<bool> CreateUser(string userName, string email, string password)
    {
        var user = new User
        {
            UserName = userName,
            Email = email,
            Password = password
        };

        _context.Users.Add(user);
        var result = await _context.SaveChangesAsync();

        return result > 0;
    }
}
