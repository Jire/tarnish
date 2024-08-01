package com.osroyale;

final class Projectile extends Renderable {

	public final int plane;
	public final int anInt1561;
	public final int anInt1562;
	public final int anInt1563;
	public final int anInt1564;
	public boolean aBoolean1567;
	private final Graphic aSpotAnim_1568;
	private int anInt1569;
	private int anInt1570;
	private int nextAnimFrameId;


	public Projectile(int plane, int j, int l, int i1, int j1, int k1, int l1) {
		aSpotAnim_1568 = Graphic.graphics[i1];
		this.plane = plane;
		anInt1561 = l1;
		anInt1562 = k1;
		anInt1563 = j1;
		anInt1564 = j + l;
		aBoolean1567 = false;
	}

	public Model getRotatedModel() {
		Model model = aSpotAnim_1568.getModel();
		if (model == null) {
			return null;
		}
		int frame = aSpotAnim_1568.animationSequence.primaryFrameIds[anInt1569];
		Model model_1 = new Model(true, Frame.hasAlphaTransform(frame), false, model);
		if (!aBoolean1567) {
			model_1.generateBones();
			model_1.interpolate(frame);
			model_1.groupedTriangleLabels = null;
			model_1.groupedVertexLabels = null;
		}
		if (aSpotAnim_1568.resizeXY != 128 || aSpotAnim_1568.resizeZ != 128) {
			model_1.scale(aSpotAnim_1568.resizeXY, aSpotAnim_1568.resizeXY, aSpotAnim_1568.resizeZ);
		}
		if (aSpotAnim_1568.rotation != 0) {
			if (aSpotAnim_1568.rotation == 90) {
				model_1.rotate90Degrees();
			}
			if (aSpotAnim_1568.rotation == 180) {
				model_1.rotate90Degrees();
				model_1.rotate90Degrees();
			}
			if (aSpotAnim_1568.rotation == 270) {
				model_1.rotate90Degrees();
				model_1.rotate90Degrees();
				model_1.rotate90Degrees();
			}
		}
		model_1.light(84 + aSpotAnim_1568.modelBrightness, 1550 + aSpotAnim_1568.modelShadow, -30, -50, -30, false);
		return model_1;
	}

	public void method454(int i) {
		final boolean tween = Client.instance.interpolateGraphicAnimations;
		for (anInt1570 += i; anInt1570 > aSpotAnim_1568.animationSequence.duration(anInt1569); ) {
			anInt1570 -= aSpotAnim_1568.animationSequence.duration(anInt1569) + 1;
			anInt1569++;
			if (anInt1569 >= aSpotAnim_1568.animationSequence.frameCount && (anInt1569 < 0 || anInt1569 >= aSpotAnim_1568.animationSequence.frameCount)) {
				anInt1569 = 0;
				aBoolean1567 = true;
			}
			if (tween) {
				nextAnimFrameId = anInt1569 + 1;
			}
			if (nextAnimFrameId >= aSpotAnim_1568.animationSequence.frameCount) {
				nextAnimFrameId = -1;
			}
		}
	}
}