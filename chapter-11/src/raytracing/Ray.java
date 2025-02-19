package raytracing;

public class Ray {
    public Vec3 o; // 源点
    public Vec3 d; // 方向

    public Ray() {}
    public Ray(Vec3 origin, Vec3 direction) { o = origin; d = direction; }

    public Vec3 origin() { return o; }
    public Vec3 direction() { return d; }

    // p(t) = A + t * B 即：t时刻光线的位置
    public Vec3 point_at_parameter(float t) { return o.Add(d.Scale(t)); };
}
