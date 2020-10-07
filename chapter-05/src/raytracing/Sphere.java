package raytracing;

public class Sphere extends Hitable {
    Vec3 center;
    float radius;

    public Sphere() {}
    public Sphere(Vec3 center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public boolean hit(Ray r, float t_min, float t_max, HitRecord rec) {
        Vec3 oc = r.origin().Subtract(center);
        float a = r.direction().dot(r.direction());
        float b = 2.0f * oc.dot(r.direction());
        float c = oc.dot(oc) - radius * radius;
        float discriminant = b * b - 4 * a * c; // 二元一次方程根的判别式 b^2 - 4ac

        if (discriminant > 0) {
            float discFactor = (float)Math.sqrt(discriminant);
            float temp1 = (-b - discFactor) / (2.0f * a);
            float temp2 = (-b + discFactor) / (2.0f * a);
            float temp = Float.MAX_VALUE;

            if (temp1 < t_max && temp1 > t_min) {
                temp = temp1;
            }

            // 优先选取符合范围，且较小的撞击点
            if (temp != Float.MAX_VALUE && temp2 < t_max && temp2 > t_min) {
                temp = temp2;
            }

            if (temp != Float.MAX_VALUE) {
                rec.t = temp;
                rec.p = r.point_at_parameter(rec.t);
                rec.normal = (rec.p.Subtract(center)).Scale(1.0f/radius);
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }

    }
}
