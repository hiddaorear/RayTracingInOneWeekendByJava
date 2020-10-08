package raytracing;

public class Metal extends Material {
    Vec3 albedo; // 反射率
    float fuzz; // 镜面模糊

    public Metal(Vec3 albedo, float f) {
        this.albedo = albedo;
        if (f < 1) {
            this.fuzz = f;
        } else {
            this.fuzz = 1;
        }
    }

    @Override
    public boolean scatter(Ray r, HitRecord rec, Wrapper wrapper) {
        Vec3 ref = reflect(r.direction(), rec.normal.normalize());
        wrapper.scattered = new Ray(rec.p, ref.Add(randomInUintSphere().Scale(fuzz))); // p -> ref
        wrapper.attenuation = albedo;
        return (ref.dot(rec.normal) > 0);
    }

    /**
     * 反射光线向量
     * @param v 入射光线
     * @param n 撞击点的法向量
     * @return 反射光线
     */
    private Vec3 reflect(Vec3 v, Vec3 n) {
        // v - 2 * dot(v, n) * n
        return v.Subtract(n.Scale(v.dot(n)*2));
    }
}
