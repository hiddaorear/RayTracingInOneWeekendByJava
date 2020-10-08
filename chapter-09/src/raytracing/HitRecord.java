package raytracing;

public class HitRecord {
    public float t; // 相撞的时间
    public Vec3 p; // 相撞的坐标
    public Vec3 normal; // 相撞点的法向量
    public Material matPtr; // 撞击的材料

    public HitRecord() {
        t = 0;
        p = new Vec3(0, 0, 0);
        normal = new Vec3(0, 0, 0);
    }
}
