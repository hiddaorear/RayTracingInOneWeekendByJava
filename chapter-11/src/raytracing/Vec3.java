package raytracing;

import java.util.Arrays;

public class Vec3 {
    public float[] e  = new float[3];

    public Vec3() {}
    public Vec3(float x, float y, float z) {
        e[0] = x;
        e[1] = y;
        e[2] = z;
    }

    public float x() { return e[0]; }
    public float y() { return e[1]; }
    public float z() { return e[2]; }

    // 向量求和
    public Vec3 Add(Vec3 a, Vec3 b) {
        return new Vec3(a.e[0] + b.e[0], a.e[1] + b.e[1], a.e[2] + b.e[2]);
    }
    public Vec3 Add(Vec3 a) {
        return new Vec3(e[0] + a.e[0], e[1] + a.e[1], e[2] + a.e[2]);
    }

    // 向量相减
    public Vec3 Subtract(Vec3 a, Vec3 b) {
        return new Vec3(a.e[0] - b.e[0], a.e[1] - b.e[1], a.e[2] - b.e[2]);
    }
    public Vec3 Subtract(Vec3 b) {
        return new Vec3(e[0] - b.e[0], e[1] - b.e[1], e[2] - b.e[2]);
    }

    // 向量数乘
    public Vec3 Scale(Vec3 a, float b) {
        return new Vec3(a.e[0] * b, a.e[1] * b, a.e[2] * b);
    }
    public Vec3 Scale(float b) {
        return new Vec3(e[0] * b, e[1] * b, e[2] * b);
    }

    // 矩阵相乘
    public Vec3 Multiply(Vec3 a, Vec3 b) {
        return new Vec3(a.e[0] * b.e[0], a.e[1] * b.e[1], a.e[2] * b.e[2]);
    }
    public Vec3 Multiply(Vec3 b) {
        return new Vec3(e[0] * b.e[0], e[1] * b.e[1], e[2] * b.e[2]);
    }


    // 向量模长
    public float length() {
        return (float)Math.sqrt(
                Math.pow((double)e[0], 2.0) +
                        Math.pow((double)e[1], 2.0) +
                        Math.pow((double)e[2], 2.0)
        );
    }

    // 模长的平方
    public float sqrLength() {
        return (float)(Math.pow((double)e[0], 2.0) +
                Math.pow((double)e[1], 2.0) +
                Math.pow((double)e[2], 2.0))
        ;
    }

    // 向量归一化
    public Vec3 normalize(Vec3 v) {
       float length = v.length();
       return new Vec3(v.x()/length, v.y()/length, v.z()/length);
    }
    public Vec3 normalize() {
       float length = this.length();
       return new Vec3(e[0]/length, e[1]/length, e[2]/length);
    }

    // 向量点乘
    public float dot(Vec3 a, Vec3 b) {
        return a.x() * b.x() + a.y() * b.y() + a.z() * b.z();
    }
    public float dot(Vec3 b) {
        return e[0] * b.x() + e[1] * b.y() + e[2] * b.z();
    }

    // 向量叉乘
    public Vec3 cross(Vec3 a, Vec3 b) {
        return new Vec3(
                a.y() * b.z() - a.z() * b.y(),
                a.z() * b.x() - a.x() * b.z(),
                a.x() * b.y() - a.y() * b.x()
        );
    }

    public Vec3 cross(Vec3 v)
    {
        return new Vec3(e[1] * v.z() - e[2] * v.y(),
                e[2] * v.x() - e[0] * v.z() ,
                e[0] * v.y() - e[1] * v.x());
    }

    @Override
    public String toString() {
        return "Vec3{" + "e=" + Arrays.toString(e) + "}";
    }
}
