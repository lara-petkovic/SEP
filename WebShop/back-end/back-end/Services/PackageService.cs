﻿using back_end.Data;
using back_end.Models;
using Microsoft.EntityFrameworkCore;

namespace back_end.Services
{
    public class PackageService
    {
        private readonly AppDbContext _context;

        public PackageService(AppDbContext context)
        {
            _context = context;
        }

        public Package Get(int id)
        {
            return _context.Packages.Where(p => p.Id == id).FirstOrDefault();
        }

        public Package GetPackageByNameAndPrice(string name, double price)
        {
            return _context.Packages.Where(p => p.Name == name && (double)p.Price == price).FirstOrDefault();
        }
        public async Task<List<Package>> GetPackagesAsync()
        {
            return await _context.Packages
                .Include(p => p.PackageServices)
                .ThenInclude(ps => ps.Service)
                .Select(p => new Package
                {
                    Id = p.Id,
                    Name = p.Name,
                    Price = p.Price,
                    IsBusinessPackage = p.IsBusinessPackage,
                    PackageServices = p.PackageServices.Select(ps => new Models.PackageService
                    {
                        ServiceId = ps.ServiceId,
                        Service = ps.Service
                    }).ToList()
                })
                .ToListAsync();
        }
    }
}
