package raytracing;

/**
 * 余弦辐射体，也称为朗伯辐射体(Lambert radiator)，指的是发光强度的空间分布符合余弦定律的发光体（不论是自发光或是反射光），其在不同角度的辐射强度会依余弦公式变化，角度越大强度越弱。
 */

public class Lambertian extends Material {
    Vec3 albedo; // 反射率，材料系数

    public Lambertian() {}

    public Lambertian(Vec3 albedo) {
        this.albedo = albedo;
    }

    public boolean scatter(Ray r, HitRecord rec, Wrapper wrapper) {
        Vec3 target = rec.p.Add(rec.normal).Add(randomInUintSphere());
        wrapper.scattered = new Ray(rec.p, target.Subtract(rec.p));
        wrapper.attenuation = albedo;
        return true;
    }
}
