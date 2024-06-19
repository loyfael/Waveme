var builder = WebApplication.CreateBuilder(args);

// Add graphql services
builder.Services.AddGraphQLServer()
    .AddQueryType<Query>();

var app = builder.Build();

// Routing
app.UseRouting();

// Configure graphql using
app.MapGraphQL();

app.Run();
