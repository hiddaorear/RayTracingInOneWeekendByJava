package raytracing;

public abstract class Material {
    public abstract boolean scatter(Ray r, HitRecord rec, Wrapper wrapper);

    /**
     * 生成一个单位球内的随机坐标
     * @return 单位球内的随机坐标
     */
    public Vec3 randomInUintSphere() {
        Vec3 p;
        do {
            p = new Vec3((float)(Math.random()), (float)(Math.random()), (float)(Math.random())).Scale(2.0f).Subtract(new Vec3(1.0f, 1.0f, 1.0f));
        } while (p.dot(p) >= 1.0f);
        return p;
    }
}
