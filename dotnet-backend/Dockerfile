FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS base
WORKDIR /app
EXPOSE 5000

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src
COPY ["QCT.UserManagement.csproj", "."]
RUN dotnet restore "QCT.UserManagement.csproj"
COPY . .
WORKDIR "/src"
RUN dotnet build "QCT.UserManagement.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "QCT.UserManagement.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENV ASPNETCORE_URLS=http://+:5000
ENTRYPOINT ["dotnet", "QCT.UserManagement.dll"]